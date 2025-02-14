package icon.factory;

import icon.CustomIconType;
import icon.Icon;
import icon.iconimpl.FileIcon;
import icon.iconimpl.FolderIcon;

import java.util.HashMap;
import java.util.Map;

import static icon.CustomIconType.FILE_ICON;
import static icon.CustomIconType.FOLDER_ICON;

public class IconFactory {
  private static final Map<CustomIconType, Icon> shapeMap=new HashMap<>();

  public static Icon getIcon(CustomIconType type){
      if(!shapeMap.containsKey(type)){
          switch (type){
              case FOLDER_ICON:{
                  shapeMap.put(type,new FolderIcon());
                  break;
              }
              case FILE_ICON:{
                  shapeMap.put(type,new FileIcon());
                  break;
              }
          }
          }
      return shapeMap.get(type);
  }
}
