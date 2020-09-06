package att;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CsvViewGUI {
    private JLabel lblClass;
    private JFrame frmDailyAttendanceAssistant;
    private JFileChooser openFileChooser;
    private JSlider slider;
    private JList jList;
    private JLabel lblScroller;
    private JLabel lblFileChosen;
    private JLabel lblThreshold;
    private JLabel lblAbsent;
    private JButton btnCheckFolder;
    private CsvContoller csvContoller;
    private int threshold;
    private static String zoomReportFolderPath = "";
    private static String schoologyRosterFilePath = "";

    public static void main(String[] args) {
        schoologyRosterFilePath = args[0];
        zoomReportFolderPath = args[1];

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CsvViewGUI window = new CsvViewGUI();
                    window.frmDailyAttendanceAssistant.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CsvViewGUI() {
        csvContoller = new CsvContoller(zoomReportFolderPath, schoologyRosterFilePath, 25);
        initialize();
        openFileChooser = new JFileChooser();
        openFileChooser.setFileFilter(new FileNameExtensionFilter("Comma Separator Values", "csv"));
    }

    private void initialize() {
        frmDailyAttendanceAssistant = new JFrame();
        frmDailyAttendanceAssistant.setTitle("Daily Attendance Assistant");
        frmDailyAttendanceAssistant.setResizable(false);
        frmDailyAttendanceAssistant.setBounds(100, 100, 450, 703);
        frmDailyAttendanceAssistant.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDailyAttendanceAssistant.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("Open File...");
        btnNewButton.setEnabled(false);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                lblClass.setText("Clicked");
                int returnValue = openFileChooser.showOpenDialog(frmDailyAttendanceAssistant);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    //takeAttendanceByFileLatestModified(openFileChooser.getSelectedFile().getAbsolutePath()); // good if the open button will be activated
                    CsvContoller csvContoller = new CsvContoller(zoomReportFolderPath, schoologyRosterFilePath, 25);
                } else {
                    lblFileChosen.setText("No File Chosen!");
                }
            }
        });
        btnNewButton.setBounds(28, 137, 97, 25);
        frmDailyAttendanceAssistant.getContentPane().add(btnNewButton);

        jList = new JList();
        jList.setVisibleRowCount(15);
        jList.setFont(new Font("Tahoma", Font.PLAIN, 16));
        jList.setBounds(28, 283, 375, 344);
        frmDailyAttendanceAssistant.getContentPane().add(jList);

        lblAbsent = new JLabel("Number of Absent Students: Report Not Generated Yet!");
        lblAbsent.setBounds(28, 254, 375, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblAbsent);

        lblThreshold = new JLabel("Min. Required Class Time: N.A.");
        lblThreshold.setBounds(28, 225, 375, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblThreshold);

        lblClass = new JLabel("Class: Not Chosen Yet!");
        lblClass.setBounds(28, 197, 375, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblClass);

        lblScroller = new JLabel("25");
        lblScroller.setBounds(246, 53, 56, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblScroller);

        lblFileChosen = new JLabel("No File Chosen Yet!");
        lblFileChosen.setBounds(137, 141, 224, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblFileChosen);

        slider = new JSlider();
        slider.setMinimum(1);
        slider.setValue(30);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                threshold = slider.getValue();
                lblScroller.setText(String.valueOf(threshold) + "min.");
                csvContoller = new CsvContoller(zoomReportFolderPath, schoologyRosterFilePath, threshold);
                updateGUI();
            }
        });
        slider.setMaximum(50);
        slider.setBounds(34, 53, 200, 26);
        frmDailyAttendanceAssistant.getContentPane().add(slider);

        JLabel lblNewLabel = new JLabel("Choose the minimum time limit to be counted as Present");
        lblNewLabel.setBounds(28, 24, 333, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Please choose the Unique User Zoom Report file");
        lblNewLabel_1.setBounds(28, 108, 333, 16);
        frmDailyAttendanceAssistant.getContentPane().add(lblNewLabel_1);


        btnCheckFolder = new JButton("Latest File");
        btnCheckFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                csvContoller = new CsvContoller(zoomReportFolderPath, schoologyRosterFilePath, threshold); //
                updateGUI();
            }
        });
        btnCheckFolder.setBounds(300, 49, 97, 25);
        frmDailyAttendanceAssistant.getContentPane().add(btnCheckFolder);
        csvContoller = new CsvContoller(zoomReportFolderPath, schoologyRosterFilePath, threshold);
        updateGUI();
    }

    private void updateGUI() {
        csvContoller.getCsvModel();
        int filePathLength = csvContoller.getCsvModel().getTargetZoomCsvReportFileName().length();
        lblFileChosen.setText(".." + csvContoller.getCsvModel().getTargetZoomCsvReportFileName().substring(filePathLength > 33 ? filePathLength - 33 : 0));
        lblClass.setText(csvContoller.getCsvModel().getClassName());
        lblThreshold.setText("Minumum required class time: " + String.valueOf(csvContoller.getCsvModel().getThreshold()) + " min.");
        lblAbsent.setText("Number of Absences: " + csvContoller.getCsvModel().getNumOfAbsences());
        jList.setModel(csvContoller.getCsvModel().dlm);
    }
}