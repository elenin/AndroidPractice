package com.example.cvte.demotvsetting.dummy;

import com.example.cvte.demotvsetting.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "picture", R.layout.fragment_item_picture));
        addItem(new DummyItem("2", "sound", R.layout.fragment_item_sound));
        addItem(new DummyItem("3", "channel", R.layout.fragment_item_channel));
        addItem(new DummyItem("4", "epg", R.layout.fragment_item_epg));
        addItem(new DummyItem("5", "inputsource", R.layout.fragment_item_inputsource));
        addItem(new DummyItem("6", "prewindow", R.layout.fragment_item_previewwindow));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;
        public int layoutId;
        public DummyItem(String id, String content, int layoutId) {
            this.id = id;
            this.content = content;
            this.layoutId = layoutId;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
