package p03.example.li.xuncha;

import java.io.Serializable;

public class SortModel implements Serializable {

    private String name;
    private String letters;//显示拼音的首字母
    private String volid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getVolid() {
        return volid;
    }

    public void setVolid(String volid) {
        this.volid = volid;
    }
}
