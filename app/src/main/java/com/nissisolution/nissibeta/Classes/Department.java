package com.nissisolution.nissibeta.Classes;

public class Department {
    public int staff_id, department_id;
    public String position, manager_name;

    public Department(int staff_id, int department_id, String position, String manager_name) {
        this.staff_id = staff_id;
        this.department_id = department_id;
        this.position = position;
        this.manager_name = manager_name;
    }
}
