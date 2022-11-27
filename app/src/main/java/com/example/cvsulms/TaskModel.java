package com.example.cvsulms;

public class TaskModel {
    String Title, Description,SecCode, SubjCode,Subject,TeacherUid,FileId,Filelink,TaskId,FileName;

    public TaskModel() {}
    public TaskModel(String Title,String Description,String SecCode, String SubjCode,
                     String Subject, String TeacherUid,String FileId,String Filelink,String TaskId, String FileName) {
        this.Title = Title;
        this.Description = Description;
        this.SecCode = SecCode;
        this.SubjCode = SubjCode;
        this.Subject = Subject;
        this.TeacherUid = TeacherUid;
        this.FileId= FileId;
        this.Filelink= Filelink;
        this.TaskId= TaskId;
        this.FileName=FileName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSecCode() {
        return SecCode;
    }

    public void setSecCode(String secCode) {
        this.SecCode = secCode;
    }

    public String getSubjCode() {
        return SubjCode;
    }

    public void setSubjCode(String subjCode) {
        this.SubjCode = subjCode;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subj) {
        this.Subject = subj;
    }

    public String getTeacherUid() {
        return TeacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        this.TeacherUid = teacherUid;
    }

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getFilelink() {
        return Filelink;
    }

    public void setFilelink(String filelink) {
        Filelink = filelink;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
