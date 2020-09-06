package att;

import javax.swing.*;

/*
This class holds variables needed to be updated on GUI.
 */
public class CsvModel {
    int threshold;
    String targetZoomCsvReportFileName;
    String className;
    int numOfAbsences;
    //list that holds the absent students
    DefaultListModel dlm;

    public CsvModel() {}

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getTargetZoomCsvReportFileName() {
        return targetZoomCsvReportFileName;
    }

    public void setTargetZoomCsvReportFileName(String targetZoomCsvReportFileName) {
        this.targetZoomCsvReportFileName = targetZoomCsvReportFileName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getNumOfAbsences() {
        return numOfAbsences;
    }

    public void setNumOfAbsences(int numOfAbsences) {
        this.numOfAbsences = numOfAbsences;
    }

    public DefaultListModel getDlm() {
        return dlm;
    }

    public void setDlm(DefaultListModel dlm) {
        this.dlm = dlm;
    }
}
