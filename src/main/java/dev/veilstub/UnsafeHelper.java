package dev.veilstub;

import imgui.extension.texteditor.TextEditor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeHelper {
    private UnsafeHelper() {
    }

    /** Creates TextEditor without running its constructor (which loads the ImGui native). */
    public static TextEditor allocateEditor() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            return (TextEditor) unsafe.allocateInstance(TextEditor.class);
        } catch (Throwable t) {
            throw new RuntimeException("veil-imgui-stub: cannot allocate TextEditor", t);
        }
    }
}
