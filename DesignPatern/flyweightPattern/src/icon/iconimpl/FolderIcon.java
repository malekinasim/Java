package icon.iconimpl;

import icon.CustomIconType;
import icon.IconPosition;
import icon.IconSize;
import icon.Icon;

import java.awt.*;

public class FolderIcon  implements Icon {

    private final CustomIconType type = CustomIconType.FOLDER_ICON;

    @Override
    public void draw(IconSize iconSize, IconPosition iconPosition, Color color) {
        System.out.println("Drawing of " +type+
                " icon at position (" + iconPosition.getX() + ", " + iconPosition.getY() + ")"
                + " icon at position (" + iconSize.getWidth() + ", " + iconSize.getHeight() + ")");

    }
}


