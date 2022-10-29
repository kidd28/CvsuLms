package com.example.cvsulms;

public class StudentSubjectModel {
    String Section, SecCode, Yearlvl , SectionId, TeacherName, TeacherNumber, TeacherUid, Subject, SubjCode, TaskId;

    public StudentSubjectModel() {}
    public StudentSubjectModel(String Section, String SecCode, String Yearlvl , String SectionId, String TeacherName, String TeacherNumber,String TeacherUid, String Subject, String SubjCode,String TaskId){
        this.Section = Section;
        this.SecCode = SecCode;
        this.Yearlvl = Yearlvl;
        this.SectionId = SectionId;
        this.TeacherName = TeacherName;
        this.TeacherNumber = TeacherNumber;
        this.TeacherUid = TeacherUid;
        this.Subject = Subject;
        this.SubjCode = SubjCode;
        this.TaskId = TaskId;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getSecCode() {
        return SecCode;
    }

    public void setSecCode(String secCode) {
        SecCode = secCode;
    }

    public String getYearlvl() {
        return Yearlvl;
    }

    public void setYearlvl(String yearlvl) {
        Yearlvl = yearlvl;
    }

    public String getSectionId() {
        return SectionId;
    }

    public void setSectionId(String sectionId) {
        SectionId = sectionId;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getTeacherNumber() {
        return TeacherNumber;
    }

    public void setTeacherNumber(String teacherNumber) {
        TeacherNumber = teacherNumber;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSubjCode() {
        return SubjCode;
    }

    public void setSubjCode(String subjCode) {
        SubjCode = subjCode;
    }

    public String getTeacherUid() {
        return TeacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        TeacherUid = teacherUid;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }
}
