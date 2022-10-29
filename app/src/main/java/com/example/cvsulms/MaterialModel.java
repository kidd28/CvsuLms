package com.example.cvsulms;

public class MaterialModel {
    String Title, Description,SecCode, SubjCode,subj,teacherUid;

    public MaterialModel() {}
    public MaterialModel(String Title,String Description,String SecCode, String SubjCode, String subj, String teacherUid) {
        this.Title = Title;
        this.Description = Description;
        this.SecCode = SecCode;
        this.SubjCode = SubjCode;
        this.subj = subj;
        this.teacherUid = teacherUid;
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

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getTeacherUid() {
        return teacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        this.teacherUid = teacherUid;
    }
}
