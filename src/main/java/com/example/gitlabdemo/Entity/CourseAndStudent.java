package com.example.gitlabdemo.Entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "courseandstudent")
@Data
@ToString
public class CourseAndStudent {
    @Id
    @GeneratedValue
    private Long csid;

    // 学生id
    @Column(name = "sid")
    private Long sid;

    // 课程分组的id
    @Column(name = "cgid")
    private Long cgid;

    @Column(name = "cid")
    private Long cid;

    @Column(name = "utype")
    private Long utype;

    @Column(name = "join_time")
    private Date joinTime;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public CourseAndStudent(){

    }

    public CourseAndStudent(Long cgid, Long sid){
        this.cgid = cgid;
        this.sid = sid;
    }

    public Long getCgid() {
        return cgid;
    }

    public void setCgid(Long cgid) {
        this.cgid = cgid;
    }

    public void setUtype(Long utype) {
        this.utype = utype;
    }

    public Long getUtype() {
        return utype;
    }

    public Long getCsid() {
        return csid;
    }

    public void setCsid(Long csid) {
        this.csid = csid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
}
