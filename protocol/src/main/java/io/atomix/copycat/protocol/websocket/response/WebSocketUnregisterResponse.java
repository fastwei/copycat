/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package io.atomix.copycat.protocol.websocket.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.atomix.copycat.error.CopycatError;
import io.atomix.copycat.protocol.websocket.request.WebSocketKeepAliveRequest;
import io.atomix.copycat.protocol.websocket.request.WebSocketUnregisterRequest;
import io.atomix.copycat.protocol.response.UnregisterResponse;

import java.util.Objects;

/**
 * Session unregister response.
 * <p>
 * Session unregister responses are sent in response to a {@link WebSocketUnregisterRequest}.
 * If the response is successful, that indicates the session was successfully unregistered. For unsuccessful
 * unregister requests, sessions can still be expired by simply haulting {@link WebSocketKeepAliveRequest}s
 * to the cluster.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class WebSocketUnregisterResponse extends WebSocketSessionResponse implements UnregisterResponse {
  @JsonCreator
  protected WebSocketUnregisterResponse(@JsonProperty("id") long id, @JsonProperty("status") Status status, @JsonProperty("error") CopycatError error) {
    super(id, status, error);
  }

  @Override
  @JsonGetter("type")
  public Type type() {
    return Type.UNREGISTER_RESPONSE;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), status);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof WebSocketUnregisterResponse) {
      WebSocketUnregisterResponse response = (WebSocketUnregisterResponse) object;
      return response.status == status;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[status=%s]", getClass().getSimpleName(), status);
  }

  /**
   * Status response builder.
   */
  public static class Builder extends WebSocketSessionResponse.Builder<UnregisterResponse.Builder, UnregisterResponse> implements UnregisterResponse.Builder {
    public Builder(long id) {
      super(id);
    }

    @Override
    public UnregisterResponse build() {
      return new WebSocketUnregisterResponse(id, status, error);
    }
  }
}