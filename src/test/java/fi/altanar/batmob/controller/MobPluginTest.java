package fi.altanar.batmob.controller;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import com.mythicscape.batclient.interfaces.BatWindow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MobPluginTest {

    @Test
    void testSearchText() {
        assertEquals("dragon", MobPlugin.searchText("search dragon"));
        assertEquals("big red dragon", MobPlugin.searchText("search big red dragon "));
        assertEquals("", MobPlugin.searchText("search"));
        assertEquals("", MobPlugin.searchText("search "));
    }

    @Test
    void testToggle() throws Exception {
        MobPlugin plugin = new MobPlugin();
        BatWindow win = mock(BatWindow.class);
        Field f = MobPlugin.class.getDeclaredField("clientWin");
        f.setAccessible(true);
        f.set(plugin, win);

        when(win.isVisible()).thenReturn(true);
        plugin.toggle();
        verify(win).setVisible(false);

        when(win.isVisible()).thenReturn(false);
        plugin.toggle();
        verify(win).setVisible(true);
    }

    @Test
    void testToggleBeforeLoad() {
        // no window yet, nothing to do
        new MobPlugin().toggle();
    }
}
