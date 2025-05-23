package bean;

import java.util.Date;

/**
 * 社員情報bean.
 * 
 */
public class EmployeeBean {
    // 社員コード
    private int employeeId;
    // 社員名
    private String employeeName;
    // 身長
    private float height;
    // eメール
    private String email;
    // 体重
    private float weight;
    // 入社年
    private int hireFiscalYear;
    // 誕生日
    private Date birthday;
    // 血液型
    private String bloodType;
    
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public int getHireFiscalYear() {
        return hireFiscalYear;
    }
    public void setHireFiscalYear(int hireFiscalYear) {
        this.hireFiscalYear = hireFiscalYear;
    }
    public Date getBithday() {
        return birthday;
    }
    public void setBithday(Date bithday) {
        this.birthday = bithday;
    }
    public String getBloodType() {
        return bloodType;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

}
