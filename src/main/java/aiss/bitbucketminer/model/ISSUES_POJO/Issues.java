
package aiss.bitbucketminer.model.ISSUES_POJO;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({
    "values",
    "pagelen",
    "size",
    "page",
    "next"
})
public class Issues {

    @JsonProperty("values")
    private List<Value> values;
    @JsonProperty("pagelen")
    private Integer pagelen;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("next")
    private String next;

    @JsonProperty("values")
    public List<Value> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<Value> values) {
        this.values = values;
    }

    @JsonProperty("pagelen")
    public Integer getPagelen() {
        return pagelen;
    }

    @JsonProperty("pagelen")
    public void setPagelen(Integer pagelen) {
        this.pagelen = pagelen;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonProperty("next")
    public String getNext() {
        return next;
    }

    @JsonProperty("next")
    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Issues.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("values");
        sb.append('=');
        sb.append(((this.values == null)?"<null>":this.values));
        sb.append(',');
        sb.append("pagelen");
        sb.append('=');
        sb.append(((this.pagelen == null)?"<null>":this.pagelen));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(((this.size == null)?"<null>":this.size));
        sb.append(',');
        sb.append("page");
        sb.append('=');
        sb.append(((this.page == null)?"<null>":this.page));
        sb.append(',');
        sb.append("next");
        sb.append('=');
        sb.append(((this.next == null)?"<null>":this.next));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
