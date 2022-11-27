package com.example.cvsulms;

public class MaterialModel {
    String Title, Description,SecCode, SubjCode,Subject,TeacherUid,FileId,Filelink,MaterialId,FileName;

    public MaterialModel() {}
    public MaterialModel(String Title,String Description,String SecCode, String SubjCode,
                         String Subject, String TeacherUid,String FileId,String Filelink,String MaterialId, String FileName) {
        this.Title = Title;
        this.Description = Description;
        this.SecCode = SecCode;
        this.SubjCode = SubjCode;
        this.Subject = Subject;
        this.TeacherUid = TeacherUid;
        this.FileId= FileId;
        this.Filelink= Filelink;
        this.MaterialId= MaterialId;
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
        SecCode = secCode;
    }

    public String getSubjCode() {
        return SubjCode;
    }

    public void setSubjCode(String subjCode) {
        SubjCode = subjCode;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTeacherUid() {
        return TeacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        TeacherUid = teacherUid;
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

    public String getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(String materialId) {
        MaterialId = materialId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
