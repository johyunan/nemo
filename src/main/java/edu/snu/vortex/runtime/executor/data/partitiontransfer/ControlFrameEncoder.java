/*
 * Copyright (C) 2017 Seoul National University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.snu.vortex.runtime.executor.data.partitiontransfer;

import edu.snu.vortex.runtime.common.comm.ControlMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import javax.inject.Inject;
import java.util.List;

/**
 * Encodes a control frame into bytes.
 *
 * @see FrameDecoder
 */
@ChannelHandler.Sharable
final class ControlFrameEncoder extends MessageToMessageEncoder<ControlMessage.DataTransferControlMessage> {

  static final int TYPE_LENGTH = Short.BYTES;
  static final int UNUSED_LENGTH = Short.BYTES;
  // the length of a frame body (not the entire frame) is stored in 4 bytes
  static final int BODYLENGTH_LENGTH = Integer.BYTES;
  static final int HEADER_LENGTH = TYPE_LENGTH + UNUSED_LENGTH + BODYLENGTH_LENGTH;

  /**
   * Private constructor.
   */
  @Inject
  private ControlFrameEncoder() {
  }

  @Override
  protected void encode(final ChannelHandlerContext ctx,
                        final ControlMessage.DataTransferControlMessage in,
                        final List<Object> out) {
    final byte[] frameBody = in.toByteArray();
    out.add(ctx.alloc().ioBuffer(HEADER_LENGTH, HEADER_LENGTH).writeShort(FrameDecoder.CONTROL_TYPE)
        .writeZero(UNUSED_LENGTH).writeInt(frameBody.length));
    out.add(Unpooled.wrappedBuffer(frameBody));
  }
}
