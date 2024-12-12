package com.nissisolution.nissibeta.Supports;

import com.nissisolution.nissibeta.Classes.AdminCompOffHoliday;
import com.nissisolution.nissibeta.Classes.AdminLeave;
import com.nissisolution.nissibeta.Classes.AdminMissedPunch;
import com.nissisolution.nissibeta.Classes.CompOffHoliday;
import com.nissisolution.nissibeta.Classes.Leave;
import com.nissisolution.nissibeta.Classes.MissedPunch;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public static List<MissedPunch> filterMissedPunch(List<MissedPunch> list, int type) {
        List<MissedPunch> missedPunchList = new ArrayList<>();
        switch (type) {
            case 0:
                for (MissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 0) {
                        missedPunchList.add(missedPunch);
                    }
                }

                for (MissedPunch missedPunch :
                        list) {
                    if (missedPunch.status != 0) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 1:
                for (MissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 0) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 2:
                for (MissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 1) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 3:
                for (MissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 2) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            default:
                break;
        }
        return missedPunchList;
    }

    public static List<AdminMissedPunch> filterAdminMissedPunch(List<AdminMissedPunch> list, int type) {
        List<AdminMissedPunch> missedPunchList = new ArrayList<>();
        switch (type) {
            case 0:
                for (AdminMissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 0) {
                        missedPunchList.add(missedPunch);
                    }
                }

                for (AdminMissedPunch missedPunch :
                        list) {
                    if (missedPunch.status != 0) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 1:
                for (AdminMissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 0) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 2:
                for (AdminMissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 1) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            case 3:
                for (AdminMissedPunch missedPunch :
                        list) {
                    if (missedPunch.status == 2) {
                        missedPunchList.add(missedPunch);
                    }
                }
                break;
            default:
                break;
        }
        return missedPunchList;
    }

    public static List<Leave> filterLeave(List<Leave> list, int type) {
        List<Leave> leaveList = new ArrayList<>();
        switch (type) {
            case 0:
                for (Leave leave :
                        list) {
                    if (leave.status == 0) {
                        leaveList.add(leave);
                    }
                }

                for (Leave leave :
                        list) {
                    if (leave.status != 0) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 1:
                for (Leave leave :
                        list) {
                    if (leave.status == 0) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 2:
                for (Leave leave :
                        list) {
                    if (leave.status == 1) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 3:
                for (Leave leave :
                        list) {
                    if (leave.status == 2) {
                        leaveList.add(leave);
                    }
                }
                break;
            default:
                break;
        }
        return leaveList;
    }

    public static List<AdminLeave> filterAdminLeave(List<AdminLeave> list, int type) {
        List<AdminLeave> leaveList = new ArrayList<>();
        switch (type) {
            case 0:
                for (AdminLeave leave :
                        list) {
                    if (leave.status == 0) {
                        leaveList.add(leave);
                    }
                }

                for (AdminLeave leave :
                        list) {
                    if (leave.status != 0) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 1:
                for (AdminLeave leave :
                        list) {
                    if (leave.status == 0) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 2:
                for (AdminLeave leave :
                        list) {
                    if (leave.status == 1) {
                        leaveList.add(leave);
                    }
                }
                break;
            case 3:
                for (AdminLeave leave :
                        list) {
                    if (leave.status == 2) {
                        leaveList.add(leave);
                    }
                }
                break;
            default:
                break;
        }
        return leaveList;
    }

    public static List<CompOffHoliday> filterCompOff(List<CompOffHoliday> list, int type) {
        List<CompOffHoliday> compOffHolidayList = new ArrayList<>();
        switch (type) {
            case 0:
                for (CompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }

                for (CompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status != 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 1:
                for (CompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 2:
                for (CompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 1) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 3:
                for (CompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 2) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            default:
                break;
        }

        return compOffHolidayList;
    }

    public static List<AdminCompOffHoliday> filterAdminCompOff(List<AdminCompOffHoliday> list, int type) {
        List<AdminCompOffHoliday> compOffHolidayList = new ArrayList<>();
        switch (type) {
            case 0:
                for (AdminCompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }

                for (AdminCompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status != 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 1:
                for (AdminCompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 0) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 2:
                for (AdminCompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 1) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            case 3:
                for (AdminCompOffHoliday compOffHoliday :
                        list) {
                    if (compOffHoliday.status == 2) {
                        compOffHolidayList.add(compOffHoliday);
                    }
                }
                break;
            default:
                break;
        }

        return compOffHolidayList;
    }

}
