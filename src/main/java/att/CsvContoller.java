package att;

import org.apache.commons.validator.routines.EmailValidator;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvContoller {
    private CsvModel csvModel;
    private BufferedReader bf2;
    private List<Student> students;
    private Map<String, ArrayList<Integer>> map;
    private Map<String, Integer> count;
    private DefaultListModel<String> dlm;

    public CsvContoller(String zoomReportFolderPath, String schoologyRosterFilePath, int threshold) {
        csvModel = new CsvModel();
        takeAttendance(zoomReportFolderPath, schoologyRosterFilePath, threshold);
    }

    public CsvModel getCsvModel() {
        return csvModel;
    }

    private boolean isZoomCsvReportValid(String fullZoomCsvReportFilePath) {
        String line = "";
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new FileReader(fullZoomCsvReportFilePath));
            while ((line = bReader.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length != 3)
                    return false;
                if (!(EmailValidator.getInstance().isValid(arr[1]) || arr[1].equalsIgnoreCase("user email")))
                    return false;
                if (!(Character.isDigit(arr[2].charAt(0)) || arr[2].equalsIgnoreCase("Total Duration (Minutes)")))
                    return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private PriorityQueue<Object[]> getAllZoomCsvFileListByModificationDate(String zoomReportFolderPath) {
        //by using the path retrieved, get the csv files inside the FOLDER
        PriorityQueue<Object[]> pqueue = new PriorityQueue<Object[]>(new TheComparator());

        try {
            File f = new File(zoomReportFolderPath);

            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File f, String name) {
                    //find only .csv files
                    return name.toLowerCase().endsWith(".csv");
                }
            };

            // this time using a File array instead of a String array
            File[] files = f.listFiles(filter);

            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                Object[] objArr = new Object[2];
                objArr[0] = files[i].getName();
                objArr[1] = files[i].lastModified();
                //add all files and their time in the priority que
                pqueue.add(objArr);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return pqueue;
    }

    private void loadClassRosterForTeachersCsv(String schoologyRosterFilePath) {
        students = new ArrayList<>();
        map = new HashMap<>();
        String line = "";
        String clazz = "";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(schoologyRosterFilePath));
            while ((line = bf.readLine()) != null) {
                List<String> values = new ArrayList<String>();
                Pattern pattern = Pattern.compile("(?:^|,)(?:\\s*\"([^\"]*)\"\\s*|([^,]*))(?=,|$)");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String value = matcher.group(1);
                    if (value == null)
                        value = matcher.group(2).trim();
                    if (!value.equals(""))
                        values.add(value);
                }
                if (values.size() < 3) continue;
                else if (values.size() > 15) clazz = values.get(1);
                else {
                    String email = values.get(4).toLowerCase();
                    if (!map.containsKey(email)) {
                        map.put(email, new ArrayList<Integer>());
                    }
                    map.get(email).add(students.size());
                    students.add(new Student(clazz, values.get(0), values.get(2), values.get(3), email, 0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStudentCountPerClass(String fullPathToTargetCsv) {
        count = new HashMap<>();
        try {
            bf2 = new BufferedReader(new FileReader(fullPathToTargetCsv));
            String line = "";

            while ((line = bf2.readLine()) != null) {
                String[] arr2 = line.split(",");
                if (map.get(arr2[1].toLowerCase()) == null)
                    continue;
                for (int index : map.get(arr2[1].toLowerCase())) {
                    Student temp = students.get(index);
                    temp.setDuration(Integer.parseInt(arr2[2]));
                    students.set(index, temp);
                    count.put(temp.clazz, count.getOrDefault(temp.clazz, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentClass() {
        String currentClass = "";
        int maxNumOfAttendees = 0;
        for (String clazzz : count.keySet()) {
            if (maxNumOfAttendees < count.get(clazzz)) {
                maxNumOfAttendees = count.get(clazzz);
                currentClass = clazzz;
            }
        }
        csvModel.setClassName(currentClass);
        return currentClass;
    }

    private DefaultListModel<String> listAllAbsentStudents(int threshold) {
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        String currentClass = getCurrentClass();

        int absentCount = 0;
        for (Student stu : students) {
            if (stu.clazz.contains(currentClass) && stu.duration < threshold) {
                dlm.addElement(++absentCount + ". " + stu.fullName + "  [" + stu.duration + "]");
                System.out.println("A: " + stu.fullName);
            }
        }
        csvModel.setNumOfAbsences(dlm.size());
        csvModel.setThreshold(threshold);
        csvModel.setDlm(dlm);
        return dlm;
    }

    private String findLatestValidZoomCsvReportFullPath(String zoomReportFolderPath) {
        /*
         Priority queue to holds the file name with the highest long value
         And all the other respective csv file names and modification date */
        PriorityQueue<Object[]> pqueue = getAllZoomCsvFileListByModificationDate(zoomReportFolderPath);
        String fullPathToTargetCsv = "";
        boolean isFileFound = false;
        while (!pqueue.isEmpty() && !isFileFound) {
            fullPathToTargetCsv = zoomReportFolderPath + String.valueOf(pqueue.poll()[0]);
            System.out.println(".. checking: " + fullPathToTargetCsv);
            if (isZoomCsvReportValid(fullPathToTargetCsv)) isFileFound = true;
        }
        return (isFileFound) ? fullPathToTargetCsv : null;
    }

    private void takeAttendance(String zoomReportFolderPath, String schoologyRosterFilePath, int threshold) {
        String fullPathToTargetCsv = findLatestValidZoomCsvReportFullPath(zoomReportFolderPath);
        if (fullPathToTargetCsv != null) {
            csvModel.setTargetZoomCsvReportFileName(fullPathToTargetCsv);
            loadClassRosterForTeachersCsv(schoologyRosterFilePath);
            setStudentCountPerClass(fullPathToTargetCsv);
            csvModel.setDlm(listAllAbsentStudents(threshold));
        } else {
            csvModel.setTargetZoomCsvReportFileName("Zoom Participant List is NOT Found!");
        }
    }
}
