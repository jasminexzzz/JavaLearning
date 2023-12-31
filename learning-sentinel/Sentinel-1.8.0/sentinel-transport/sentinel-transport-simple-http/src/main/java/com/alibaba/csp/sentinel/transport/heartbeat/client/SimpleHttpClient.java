/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.transport.heartbeat.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.csp.sentinel.log.RecordLog;

/**
 * <p>
 * A very simple HTTP client that only supports GET/POST method and plain text request body.
 * The Content-Type header is always set as <pre>application/x-www-form-urlencoded</pre>.
 * All parameters in the request will be encoded using {@link URLEncoder#encode(String, String)}.
 * 一个非常简单的HTTP客户端，只支持GET/POST方法和纯文本请求体。Content-Type报头总是设置为<pre>application/x-www-form-urlencoded</pre>。
 * 请求中的所有参数都将使用{@link URLEncoder#encode(String, String)}编码。
 * </p>
 * <p>
 * The result of a HTTP invocation will be wrapped as a {@link SimpleHttpResponse}. Content in response body
 * will be automatically decoded to string with provided charset.
 * HTTP调用的结果将被包装为{@link SimpleHttpResponse}。响应体中的内容将自动解码为具有提供的字符集的字符串。
 * </p>
 * <p>
 * This is a blocking and synchronous client, so an invocation will await the response until timeout exceed.
 * 这是一个阻塞和同步的客户端，因此调用将等待响应，直到超时。
 * </p>
 * <p>
 * Note that this is a very NAIVE client, {@code Content-Length} must be specified in the
 * HTTP response header, otherwise, the response body will be dropped. All other body type such as
 * {@code Transfer-Encoding: chunked}, {@code Transfer-Encoding: deflate} are not supported.
 * 注意，这是一个非常天真的客户端，{@code Content-Length}必须在HTTP响应头中指定，否则，响应体将被删除。其他所有body类型，
 * 如{@code Transfer-Encoding: chunked}， {@code Transfer-Encoding: deflate}都不支持。
 * </p>
 *
 * @author leyou
 */
public class SimpleHttpClient {

    /**
     * Execute a GET HTTP request.
     *
     * @param request HTTP request
     * @return the response if the request is successful
     * @throws IOException when connection cannot be established or the connection is interrupted
     */
    public SimpleHttpResponse get(SimpleHttpRequest request) throws IOException {
        if (request == null) {
            return null;
        }
        return request(request.getSocketAddress(),
            RequestMethod.GET, request.getRequestPath(), request.getParams(),
            request.getCharset(), request.getSoTimeout());
    }

    /**
     * Execute a POST HTTP request.
     *
     * @param request HTTP request
     * @return the response if the request is successful
     * @throws IOException when connection cannot be established or the connection is interrupted
     */
    public SimpleHttpResponse post(SimpleHttpRequest request) throws IOException {
        if (request == null) {
            return null;
        }
        return request(request.getSocketAddress(),
            RequestMethod.POST, request.getRequestPath(),
            request.getParams(), request.getCharset(),
            request.getSoTimeout());
    }

    private SimpleHttpResponse request(InetSocketAddress socketAddress,
                                       RequestMethod type, String requestPath,
                                       Map<String, String> paramsMap, Charset charset, int soTimeout)
        throws IOException {
        Socket socket = null;
        BufferedWriter writer;
        try {
            socket = new Socket();
            socket.setSoTimeout(soTimeout);
            socket.connect(socketAddress, soTimeout);

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
            requestPath = getRequestPath(type, requestPath, paramsMap, charset);
            writer.write(getStatusLine(type, requestPath) + "\r\n");
            if (charset != null) {
                writer.write("Content-Type: application/x-www-form-urlencoded; charset=" + charset.name() + "\r\n");
            } else {
                writer.write("Content-Type: application/x-www-form-urlencoded\r\n");
            }
            writer.write("Host: " + socketAddress.getHostName() + "\r\n");
            if (type == RequestMethod.GET) {
                writer.write("Content-Length: 0\r\n");
                writer.write("\r\n");
            } else {
                // POST method.
                String params = encodeRequestParams(paramsMap, charset);
                writer.write("Content-Length: " + params.getBytes(charset).length + "\r\n");
                writer.write("\r\n");
                writer.write(params);
            }
            writer.flush();

            SimpleHttpResponse response = new SimpleHttpResponseParser().parse(socket.getInputStream());
            socket.close();
            socket = null;
            return response;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {
                    RecordLog.warn("Error when closing {} request to {} in SimpleHttpClient", type, socketAddress, ex);
                }
            }
        }
    }

    private String getRequestPath(RequestMethod type, String requestPath,
                                  Map<String, String> paramsMap, Charset charset) {
        if (type == RequestMethod.GET) {
            if (requestPath.contains("?")) {
                return requestPath + "&" + encodeRequestParams(paramsMap, charset);
            }
            return requestPath + "?" + encodeRequestParams(paramsMap, charset);
        }
        return requestPath;
    }

    private String getStatusLine(RequestMethod type, String requestPath) {
        if (type == RequestMethod.POST) {
            return "POST " + requestPath + " HTTP/1.1";
        }
        return "GET " + requestPath + " HTTP/1.1";
    }

    /**
     * Encode and get the URL request parameters.
     *
     * @param paramsMap pair of parameters
     * @param charset   charset
     * @return encoded request parameters, or empty string ("") if no parameters are provided
     */
    private String encodeRequestParams(Map<String, String> paramsMap, Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset is not allowed to be null");
        }
        if (paramsMap == null || paramsMap.isEmpty()) {
            return "";
        }
        try {
            StringBuilder paramsBuilder = new StringBuilder();
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                paramsBuilder.append(URLEncoder.encode(entry.getKey(), charset.name()))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), charset.name()))
                    .append("&");
            }
            if (paramsBuilder.length() > 0) {
                // Remove the last '&'.
                paramsBuilder.delete(paramsBuilder.length() - 1, paramsBuilder.length());
            }
            return paramsBuilder.toString();
        } catch (Throwable e) {
            RecordLog.warn("Encode request params fail", e);
            return "";
        }
    }

    private enum RequestMethod {
        GET,
        POST
    }

}
