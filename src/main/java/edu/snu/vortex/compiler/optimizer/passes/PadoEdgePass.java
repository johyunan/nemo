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
package edu.snu.vortex.compiler.optimizer.passes;

import edu.snu.vortex.compiler.ir.attribute.Attribute;
import edu.snu.vortex.compiler.ir.DAG;
import edu.snu.vortex.compiler.ir.Edge;

import java.util.List;
import java.util.Optional;

/**
 * Pado pass for tagging edges.
 */
public final class PadoEdgePass implements Pass {
  public DAG process(final DAG dag) throws Exception {
    dag.getVertices().forEach(vertex -> {
      final Optional<List<Edge>> inEdges = dag.getInEdgesOf(vertex);
      if (inEdges.isPresent()) {
        inEdges.get().forEach(edge -> {
          if (fromTransientToReserved(edge)) {
            edge.setAttr(Attribute.Key.ChannelDataPlacement, Attribute.Memory);
            edge.setAttr(Attribute.Key.ChannelTransferPolicy, Attribute.Push);
          } else if (fromReservedToTransient(edge)) {
            edge.setAttr(Attribute.Key.ChannelDataPlacement, Attribute.File);
            edge.setAttr(Attribute.Key.ChannelTransferPolicy, Attribute.Pull);
          } else {
            if (edge.getType().equals(Edge.Type.OneToOne)) {
              edge.setAttr(Attribute.Key.ChannelDataPlacement, Attribute.Local);
              edge.setAttr(Attribute.Key.ChannelTransferPolicy, Attribute.Pull);
            } else {
              edge.setAttr(Attribute.Key.ChannelDataPlacement, Attribute.File);
              edge.setAttr(Attribute.Key.ChannelTransferPolicy, Attribute.Pull);
            }
          }
        });
      }
    });
    return dag;
  }

  private boolean fromTransientToReserved(final Edge edge) {
    return edge.getSrc().getAttr(Attribute.Key.Placement).equals(Attribute.Transient) &&
        edge.getDst().getAttr(Attribute.Key.Placement).equals(Attribute.Reserved);
  }

  private boolean fromReservedToTransient(final Edge edge) {
    return edge.getSrc().getAttr(Attribute.Key.Placement).equals(Attribute.Reserved) &&
        edge.getDst().getAttr(Attribute.Key.Placement).equals(Attribute.Transient);
  }
}
