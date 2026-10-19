package dev.veilstub.mixin;

import imgui.extension.texteditor.TextEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * В конструкторе Veil CodeEditor(String) делается:
 *   this.editor = new TextEditor();      // здесь грузится нативный ImGui и падает на aarch64
 *   this.editor.setShowWhitespaces(false);
 * Мы вместо этого оставляем editor = null и пропускаем вызов.
 * Редактор открывается только из ImGui-окон Veil, которые на этом устройстве не работают
 * (Veil сам переключается на InactiveVeilImGuiImpl).
 */
@Mixin(targets = "foundry.veil.api.client.imgui.CodeEditor", remap = false)
public abstract class CodeEditorMixin {

    @Redirect(
            method = "<init>(Ljava/lang/String;)V",
            at = @At(value = "NEW", target = "()Limgui/extension/texteditor/TextEditor;", remap = false),
            remap = false
    )
    private TextEditor veilstub$skipEditor() {
        return null;
    }

    @Redirect(
            method = "<init>(Ljava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Limgui/extension/texteditor/TextEditor;setShowWhitespaces(Z)V", remap = false),
            remap = false
    )
    private void veilstub$skipWhitespaces(TextEditor editor, boolean show) {
    }
}
