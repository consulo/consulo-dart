package consulo.dart.debugger.breakpoint;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.runner.DartLineBreakpointType;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.debug.breakpoint.XBreakpointType;
import consulo.execution.debug.breakpoint.XLineBreakpointType;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeResolver;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: line breakpoint availability goes through {@link XLineBreakpointTypeResolver}, see {@link DartLineBreakpointType#canPutAt}.
 *
 * @author VISTALL
 * @since 5/8/2016
 */
@ExtensionImpl
public class DartLineBreakpointTypeResolver implements XLineBreakpointTypeResolver {
  @RequiredReadAction
  @Override
  public @Nullable XLineBreakpointType<?> resolveBreakpointType(@NotNull Project project, @NotNull VirtualFile virtualFile, int line) {
    DartLineBreakpointType type = XBreakpointType.EXTENSION_POINT_NAME.findExtension(DartLineBreakpointType.class);
    return type != null && type.canPutAt(virtualFile, line, project) ? type : null;
  }

  @Override
  public @NotNull FileType getFileType() {
    return DartFileType.INSTANCE;
  }
}
