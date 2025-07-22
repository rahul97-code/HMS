package hms.patient.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
* This class implements a generic file name filter that allows the listing/selection
* of JPEG files.
*/
public class ImageFileFilter extends FileFilter implements java.io.FileFilter
 {
 public boolean accept(File f)
   {
   if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
   if (f.getName().toLowerCase().endsWith(".jpg")) return true;
   if (f.getName().toLowerCase().endsWith(".png")) return true;
   if(f.isDirectory())return true;
   return false;
  }
 public String getDescription()
   {
   return "JPEG files";
   }

} 