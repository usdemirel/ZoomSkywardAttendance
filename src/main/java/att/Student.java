package att;

public class Student {

    String clazz;
    String fullName;
    String gradeLevel;
    String gender;
    String email;
    int duration;

    public Student(String clazz, String fullName, String gradeLevel, String gender, String email, int duration) {
        super();
        this.clazz = clazz;
        this.fullName = fullName;
        this.gradeLevel = gradeLevel;
        this.gender = gender;
        this.email = email;
        this.duration = duration;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "att.Student [clazz=" + clazz + ", fullName=" + fullName + ", gradeLevel=" + gradeLevel + ", gender="
                + gender + ", email=" + email + ", duration=" + duration + "]";
    }
}
