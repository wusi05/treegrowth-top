package top.treegrowth.model.res;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

/**
 * @author wusi
 * @version 2017/3/31 7:03.
 */
public class PageDetail implements Serializable {

    private static final long serialVersionUID = 4078488500752732981L;
    private String id;
    private String diaryId;
    private String diaryName;
    private String name;
    @JsonFormat(shape= STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    private String mind;
    private String content;
    private String weather;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getDiaryName() {
        return diaryName;
    }

    public void setDiaryName(String diaryName) {
        this.diaryName = diaryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMind() {
        return mind;
    }

    public void setMind(String mind) {
        this.mind = mind;
    }

    public String getContent() {
        return content;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
