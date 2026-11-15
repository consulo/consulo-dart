// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.vmService.frame;

import com.google.gson.JsonElement;
import consulo.ui.image.Image;
import consulo.application.AllIcons;
import consulo.util.collection.SmartList;
import consulo.execution.debug.frame.XCompositeNode;
import consulo.execution.debug.frame.XNamedValue;
import consulo.execution.debug.frame.XNavigatable;
import consulo.execution.debug.frame.XValueChildrenList;
import consulo.execution.debug.frame.XValueGroup;
import consulo.execution.debug.frame.XValueNode;
import consulo.execution.debug.frame.XValuePlace;
import com.jetbrains.lang.dart.ide.runner.server.vmService.DartVmServiceDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.vmService.vmServiceDrivers.service.consumer.GetObjectConsumer;
import com.jetbrains.lang.dart.ide.runner.server.vmService.vmServiceDrivers.service.element.*;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

// similar to com.intellij.debugger.engine.JavaStaticGroup
class DartStaticFieldsGroup extends XValueGroup {
  private final @NotNull DartVmServiceDebugProcess myDebugProcess;
  private final @NotNull String myIsolateId;
  private final @NotNull String myClassName;
  private final @NotNull SmartList<? extends FieldRef> myFieldRefs;

  DartStaticFieldsGroup(final @NotNull DartVmServiceDebugProcess debugProcess,
                        final @NotNull String isolateId,
                        final @NotNull String className,
                        final @NotNull SmartList<? extends FieldRef> fieldsRefs) {
    super(LocalizeValue.of("static"));
    myDebugProcess = debugProcess;
    myIsolateId = isolateId;
    myClassName = className;
    myFieldRefs = fieldsRefs;
  }

  @Override
  public @NotNull String getSeparator() {
    return "";
  }

  @Override
  public String getComment() {
    return " members of " + myClassName;
  }

  @Override
  public Image getIcon() {
    return AllIcons.Nodes.Static;
  }

  @Override
  public void computeChildren(final @NotNull XCompositeNode node) {
    final AtomicInteger counter = new AtomicInteger(myFieldRefs.size());
    final XValueChildrenList list = new XValueChildrenList(myFieldRefs.size());

    for (final FieldRef fieldRef : myFieldRefs) {
      myDebugProcess.getVmServiceWrapper().getObject(myIsolateId, fieldRef.getId(), new GetObjectConsumer() {
        @Override
        public void received(Obj field) {
          final InstanceRef instanceRef = ((Field)field).getStaticValue();
          // static field may be not initialized yet, in this case this instanceRef is in fact a Sentinel
          if (instanceRef != null) {
            if ("@Instance".equals(instanceRef.getType())) {
              list.add(new DartVmServiceValue(myDebugProcess, myIsolateId, ((Field)field).getName(), instanceRef, null, fieldRef, false));
            }
            else if ("Sentinel".equals(instanceRef.getType())) {
              list.add(new XNamedValue(((Field)field).getName()) {
                @Override
                public void computeSourcePosition(@NotNull XNavigatable navigatable) {
                  DartVmServiceValue.doComputeSourcePosition(myDebugProcess, navigatable, myIsolateId, fieldRef);
                }

                @Override
                public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
                  final JsonElement valueAsString = instanceRef.getJson().get("valueAsString");
                  final String value = valueAsString == null ? "not initialized" : valueAsString.getAsString();
                  node.setPresentation(AllIcons.Nodes.Field, null, value, false);
                }
              });
            }
          }

          if (counter.decrementAndGet() == 0) {
            if (list.size() == 0) {
              node.setErrorMessage("Static fields not initialized yet");
            }
            else {
              node.addChildren(list, true);
            }
          }
        }

        @Override
        public void received(Sentinel sentinel) {
          node.setErrorMessage(sentinel.getValueAsString());
        }

        @Override
        public void onError(RPCError error) {
          node.setErrorMessage(error.getMessage());
        }
      });
    }
  }
}
