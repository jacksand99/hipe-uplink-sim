package vega.hipe;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import vega.hipe.logging.VegaLog;

public class FileUtil {
    /**
     * Create a temporary file in the application's working directory. The application's working directory is defined by
     * var.hcss.workdir if this property exists. If not, it's defined by the system property java.io.tmpdir (see
     * {@link #TEMP_DIR}).
     *
     * @param prefix - prefix for the file name
     * @param suffix - suffix for the file name
     * @return new file in a temporary directory
     * @throws IOException - if errors occur when creating the file or directory
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tmpRootFile = PathConfiguration.getTempDir();
        tmpRootFile.mkdirs();
        return File.createTempFile(prefix, suffix, tmpRootFile);
    }

    /**
     * Create a temporary directory in the application's working directory. The application's working directory is
     * defined by var.hcss.workdir if this property exists. If not, it's defined by the system property java.io.tmpdir
     * (see {@link #TEMP_DIR}).
     *
     * @param prefix - prefix for the directory name
     * @param suffix - suffix for the directory name
     * @return new directory inside a temporary directory
     * @throws IOException - if errors occur when creating the directory or its parents
     */
    public static File createTempDir(String prefix, String suffix) throws IOException {
        File file = createTempFile(prefix, suffix);
        boolean flag = file.delete();
        flag &= file.mkdir();
        if (!flag) {
            throw new IOException("Failed to create directory:  " + file);
        }
        return file;
    }
    
    /**
     * Delete a file
     * @param f file to delete
     * @return true if successful. False otherwise.
     */
    public static boolean delete(File f) {
        final String failedToDelete = "Failed to delete: ";

        if (f == null) {
            throw new IllegalArgumentException("File is null");
        } else if (f.isDirectory()) {
            try {
                FileUtils.deleteDirectory(f);
                return true;
            } catch (IOException e) {
            	VegaLog.warning(failedToDelete + f);
                return false;
            }
        } else {
            return f.delete();
        }

    }
}
