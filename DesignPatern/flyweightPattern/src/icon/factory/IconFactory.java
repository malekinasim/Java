package icon.factory;

import icon.CustomIconType;
import icon.Icon;
import icon.iconimpl.FileIcon;
import icon.iconimpl.FolderIcon;

import java.util.HashMap;
import java.util.Map;


public class IconFactory {
  private static final Map<CustomIconType, Icon> shapeMap=new HashMap<>();

  public static Icon getIcon(CustomIconType type){
      return shapeMap.computeIfAbsent(type,key-> switch (key) {
          case FOLDER_ICON -> new FolderIcon();
          case FILE_ICON -> new FileIcon();
      }
      );
      }
}