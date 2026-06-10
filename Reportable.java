package advancedproj;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface Reportable {
    
    String generateReport();

    default String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date());
    }
}