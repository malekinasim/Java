package icon;

public enum CustomIconType {
  FOLDER_ICON("FOLDER_ICON","../images/folder.png"),
      FILE_ICON("FILE_ICON","../images/file.png");
     private final String type;
     private final String ImagePath;

     CustomIconType(String type, String imagePath) {
          this.type = type;
          ImagePath = imagePath;
     }

     public String getType() {
          return type;
     }

     public String getImagePath() {
          return ImagePath;
     }

     @Override
     public String toString() {
          return " (type='" + type + '\'' +
                  ", ImagePath='" + ImagePath + '\'' +") ";
     }

}
