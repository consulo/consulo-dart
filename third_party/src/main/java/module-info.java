/**
 * @author VISTALL
 * @since 2026-11-15
 */
module com.jetbrains.lang.dart {
  requires consulo.application.api;
  requires consulo.application.content.api;
  requires consulo.application.ui.api;
  requires consulo.base.icon.library;
  requires consulo.builtin.web.server.api;
  requires consulo.code.editor.api;
  requires consulo.color.scheme.api;
  requires consulo.component.api;
  requires consulo.configurable.api;
  requires consulo.datacontext.api;
  requires consulo.disposer.api;
  requires consulo.document.api;
  requires consulo.execution.api;
  requires consulo.execution.debug.api;
  requires consulo.execution.test.api;
  requires consulo.execution.test.sm.api;
  requires consulo.file.chooser.api;
  requires consulo.file.editor.api;
  requires consulo.file.template.api;
  requires consulo.ide.api;
  // see CONSULO.md, "ide-impl usages"
  requires consulo.ide.impl;
  requires consulo.index.io;
  requires consulo.language.api;
  requires consulo.language.code.style.api;
  requires consulo.language.code.style.ui.api;
  requires consulo.language.copyright.api;
  requires consulo.language.editor.api;
  requires consulo.language.editor.refactoring.api;
  requires consulo.language.editor.ui.api;
  requires consulo.language.impl;
  requires consulo.localize.api;
  requires consulo.logging.api;
  requires consulo.module.api;
  requires consulo.module.creation.api;
  requires consulo.module.content.api;
  requires consulo.module.ui.api;
  requires consulo.navigation.api;
  requires consulo.platform.api;
  requires consulo.process.api;
  requires consulo.project.api;
  requires consulo.project.ui.api;
  requires consulo.ui.api;
  requires consulo.http.api;
  requires consulo.web.browser.api;
  requires consulo.ui.ex.api;
  requires consulo.ui.ex.awt.api;
  requires consulo.undo.redo.api;
  requires consulo.usage.api;
  requires consulo.util.collection;
  requires consulo.util.collection.primitive;
  requires consulo.util.dataholder;
  requires consulo.util.io;
  requires consulo.util.lang;
  requires consulo.util.xml.serializer;
  requires consulo.virtual.file.system.api;

  requires consulo.annotation;
  requires com.google.gson;
  requires com.google.common;
  requires io.netty.codec.http;
  requires org.apache.commons.lang3;

  requires com.intellij.xml;
  requires com.intellij.xml.html.api;
  requires org.yaml.snakeyaml;
  requires org.jetbrains.plugins.yaml;

  requires forms.rt;

  // TODO remove in future
  requires java.desktop;
  requires java.management;
  requires java.net.http;

  // exports are added while packages are ported
}
