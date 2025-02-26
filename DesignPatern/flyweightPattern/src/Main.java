import icon.CustomIconType;
import icon.Icon;
import icon.IconPosition;
import icon.IconSize;
import icon.factory.IconFactory;

import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Icon fileIcon1 = IconFactory.getIcon(CustomIconType.FILE_ICON);
        fileIcon1.draw(new IconSize(16,16),new IconPosition(100,100), Color.blue);

        Icon fileIcon2 = IconFactory.getIcon(CustomIconType.FILE_ICON);
        fileIcon2.draw(new IconSize(32,32),new IconPosition(300,200), Color.red);


        Icon folderIcon1 = IconFactory.getIcon(CustomIconType.FOLDER_ICON);
        folderIcon1.draw(new IconSize(32,32),new IconPosition(200,100), Color.blue);

        Icon folderIcon2 = IconFactory.getIcon(CustomIconType.FOLDER_ICON);
        folderIcon2.draw(new IconSize(24,24),new IconPosition(200,200), Color.red);

    }
}














