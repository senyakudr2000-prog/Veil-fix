package dev.veilstub.mixin;

import dev.veilstub.UnsafeHelper;
import imgui.extension.texteditor.TextEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Veil's CodeEditor(String) constructor does:
 *   this.editor = new TextEditor();          // loads the ImGui native and crashes on aarch64
 *   this.editor.setShowWhitespaces(false);
 * We replace the first line with an empty TextEditor object (no constructor call)
 * and skip the second one. The editor is only used by ImGui windows,
 * which are inactive on this device.
 */
@Mixin(targets = "foundry.veil.api.client.imgui.CodeEditor", remap = false)
public abstract class CodeEditorMixin {

    @Redirect(
            method = "<init>(Ljava/lang/String;)V",
            at = @At(value = "NEW", target = "()Limgui/extension/texteditor/TextEditor;", remap = false),
            remap = false
    )
    private TextEditor veilstub$skipEditor() {
        return UnsafeHelper.allocateEditor();
    }

    @Redirect(
            method = "<init>(Ljava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Limgui/extension/texteditor/TextEditor;setShowWhitespaces(Z)V", remap = false),
            remap = false
    )
    private void veilstub$skipWhitespaces(TextEditor editor, boolean show) {
    }
}
