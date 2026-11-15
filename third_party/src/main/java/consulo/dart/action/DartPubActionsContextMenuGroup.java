package consulo.dart.action;

import com.jetbrains.lang.dart.ide.actions.DartPubBuildAction;
import com.jetbrains.lang.dart.ide.actions.DartPubGetAction;
import com.jetbrains.lang.dart.ide.actions.DartPubUpgradeAction;
import consulo.annotation.component.ActionImpl;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionRefAnchor;
import consulo.ui.ex.action.AnSeparator;
import consulo.ui.ex.action.DefaultActionGroup;

/**
 * Upstream plugin.xml {@code <group id="Dart.pub.actions.context.menu.group">}.
 */
@ActionImpl(id = "Dart.pub.actions.context.menu.group", children = {
  @ActionRef(type = DartPubGetAction.class),
  @ActionRef(type = DartPubUpgradeAction.class),
  @ActionRef(type = DartPubBuildAction.class),
  @ActionRef(type = AnSeparator.class)
}, parents = {
  @ActionParentRef(value = @ActionRef(id = "EditorPopupMenu"), anchor = ActionRefAnchor.FIRST),
  @ActionParentRef(value = @ActionRef(id = "ProjectViewPopupMenu"), anchor = ActionRefAnchor.BEFORE, relatedToAction = @ActionRef(id = "CutCopyPasteGroup"))
})
public class DartPubActionsContextMenuGroup extends DefaultActionGroup {
}
