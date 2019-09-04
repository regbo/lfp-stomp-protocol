package ua.naiksoftware.stomp.provider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class OkHttpConnectionProvider extends AbstractConnectionProvider {

	public static final String TAG = "OkHttpConnProvider";

	private final String mUri;
	@NonNull
	private final Supplier<Map<String, ? extends Iterable<String>>> mConnectHttpHeaders;
	private final OkHttpClient mOkHttpClient;

	@Nullable
	private WebSocket openSocket;

	public OkHttpConnectionProvider(String uri, @Nullable Map<String, String> connectHttpHeaders,
			OkHttpClient okHttpClient) {
		this(uri, connectHttpHeaders == null ? null : () -> {
			Map<String, List<String>> map = new LinkedHashMap<>();
			for (String key : connectHttpHeaders.keySet()) {
				map.put(key, new ArrayList<>());
				map.get(key).add(connectHttpHeaders.get(key));
			}
			return map;
		}, okHttpClient);
	}

	public OkHttpConnectionProvider(String uri,
			@Nullable Supplier<Map<String, ? extends Iterable<String>>> connectHttpHeadersSupplier,
			OkHttpClient okHttpClient) {
		super();
		mUri = uri;
		mConnectHttpHeaders = connectHttpHeadersSupplier;
		mOkHttpClient = okHttpClient;
	}

	@Override
	public void rawDisconnect() {
		if (openSocket != null) {
			openSocket.close(1000, "");
		}
	}

	@Override
	protected void createWebSocketConnection() {
		Request.Builder requestBuilder = new Request.Builder().url(mUri);

		addConnectionHeadersToBuilder(requestBuilder);

		openSocket = mOkHttpClient.newWebSocket(requestBuilder.build(), new WebSocketListener() {
			@Override
			public void onOpen(WebSocket webSocket, @NonNull Response response) {
				LifecycleEvent openEvent = new LifecycleEvent(LifecycleEvent.Type.OPENED);

				TreeMap<String, String> headersAsMap = headersAsMap(response);

				openEvent.setHandshakeResponseHeaders(headersAsMap);
				emitLifecycleEvent(openEvent);
			}

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				emitMessage(text);
			}

			@Override
			public void onMessage(WebSocket webSocket, @NonNull ByteString bytes) {
				emitMessage(bytes.utf8());
			}

			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				openSocket = null;
				emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED));
			}

			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				// in OkHttp, a Failure is equivalent to a JWS-Error *and* a JWS-Close
				emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.ERROR, new Exception(t)));
				openSocket = null;
				emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED));
			}

			@Override
			public void onClosing(final WebSocket webSocket, final int code, final String reason) {
				webSocket.close(code, reason);
			}
		}

		);
	}

	@Override
	protected void rawSend(String stompMessage) {
		openSocket.send(stompMessage);
	}

	@Nullable
	@Override
	protected Object getSocket() {
		return openSocket;
	}

	@NonNull
	private TreeMap<String, String> headersAsMap(@NonNull Response response) {
		TreeMap<String, String> headersAsMap = new TreeMap<>();
		Headers headers = response.headers();
		for (String key : headers.names()) {
			headersAsMap.put(key, headers.get(key));
		}
		return headersAsMap;
	}

	private void addConnectionHeadersToBuilder(@NonNull Request.Builder requestBuilder) {
		Map<String, ? extends Iterable<String>> map = mConnectHttpHeaders == null ? null : mConnectHttpHeaders.get();
		if (map == null)
			return;
		for (String key : map.keySet()) {
			Iterable<String> vals = map.get(key);
			if (vals == null)
				continue;
			for (String val : vals)
				requestBuilder.addHeader(key, val);
		}
	}
}
