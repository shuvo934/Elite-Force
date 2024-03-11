package ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.model.SecondPageData;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class SecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText expecJoin;
    EditText actuJoin;
    EditText calcJoin;
    EditText appoinType;
    EditText revisoinType;
    EditText serviceStatus;
    EditText description;
    EditText probaPeriod;
    EditText probaComp;
    EditText workPeEdDate;
    EditText workPePeriod;
    EditText workPeComp;
    EditText quitDate;
    EditText shiftAlloc;
    EditText salaryPac;
    EditText increAmnt;
    EditText decreAmnt;
    EditText grossSalary;
    EditText cashSalary;
    EditText basicSalary;
    EditText houseRent;
    EditText medicalAllow;
    EditText conveyaAllow;
    EditText habitation;
    EditText utilty;
    EditText houseAllow;
    EditText entertain;
    EditText factoryAllow;
    EditText committedSalary;
    EditText mobileBill;
    EditText additinAllow;
    EditText fixedOt;
    EditText foodSubsidy;
    EditText otherAlloDetl;
    EditText overtimeAllowApp;
    EditText overtimeRate;
    EditText providentFundType;
    EditText providentFund;
    EditText otCalPeriod;
    EditText taxDeduc;
    EditText sitting;
    EditText lunchStatus;
    EditText transport;
    EditText pabx_office;
    EditText pabx_factory;

    EditText fesTypeBonus;
    EditText foodSubsidyType;
    EditText taxBearer;

    ArrayList<String> fesBonus;
    ArrayList<String> foodsubsidy;
    ArrayList<String> taxBear;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    ArrayList<SecondPageData> secondPageData;

    Context mContext;
    public SecondFragment(Context context) {
        this.mContext = context;
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        expecJoin = view.findViewById(R.id.expected_join);
        actuJoin = view.findViewById(R.id.actual_join);
        calcJoin = view.findViewById(R.id.calculation_join);
        appoinType = view.findViewById(R.id.appointment_type);
        revisoinType = view.findViewById(R.id.revision_type);
        serviceStatus = view.findViewById(R.id.service_status);
        description = view.findViewById(R.id.description);
        probaPeriod = view.findViewById(R.id.probation_period);
        probaComp = view.findViewById(R.id.probation_comp);
        workPeEdDate = view.findViewById(R.id.work_pe_eff_dat);
        workPePeriod = view.findViewById(R.id.work_per_period);
        workPeComp = view.findViewById(R.id.work_pe_completion);
        quitDate = view.findViewById(R.id.quit_dat);
        shiftAlloc = view.findViewById(R.id.shift_allocation);
        salaryPac = view.findViewById(R.id.salary_package);
        increAmnt = view.findViewById(R.id.increment_amnt);
        decreAmnt = view.findViewById(R.id.decrement_amnt);
        grossSalary = view.findViewById(R.id.gross_salary);
        cashSalary = view.findViewById(R.id.cash_salary);
        basicSalary = view.findViewById(R.id.basic_salary);
        houseRent = view.findViewById(R.id.house_rent);
        medicalAllow = view.findViewById(R.id.medical_allow);
        conveyaAllow = view.findViewById(R.id.conveyance_allow);
        habitation = view.findViewById(R.id.habitation);
        utilty = view.findViewById(R.id.utilities);
        houseAllow = view.findViewById(R.id.house_allow);
        entertain = view.findViewById(R.id.entertainment);
        factoryAllow = view.findViewById(R.id.factory_allow);
        committedSalary = view.findViewById(R.id.committed_salary);
        mobileBill = view.findViewById(R.id.mobile_bill_coverage);
        additinAllow = view.findViewById(R.id.additional_allow);
        fixedOt = view.findViewById(R.id.fixed_ot);
        foodSubsidy = view.findViewById(R.id.food_subsidy);
        otherAlloDetl = view.findViewById(R.id.others_allow_detl);
        overtimeAllowApp = view.findViewById(R.id.overtime_allow_applic);
        overtimeRate = view.findViewById(R.id.overtime_rate);
        providentFundType = view.findViewById(R.id.provident_fund_type);
        providentFund = view.findViewById(R.id.provident_fund);
        otCalPeriod = view.findViewById(R.id.ot_cal_period);
        taxDeduc = view.findViewById(R.id.tax_deduction);
        sitting = view.findViewById(R.id.sitting);
        lunchStatus = view.findViewById(R.id.lunch_status);
        transport = view.findViewById(R.id.transport);
        pabx_office = view.findViewById(R.id.pabx_corporate_offc);
        pabx_factory = view.findViewById(R.id.pabx_factory);

        fesTypeBonus = view.findViewById(R.id.spinner_fes_bon_type);
        foodSubsidyType = view.findViewById(R.id.spinner_food_sub_type);
        taxBearer = view.findViewById(R.id.spinner_tax_bearer);

        emp_id = userInfoLists.get(0).getEmp_id();

        secondPageData = new ArrayList<>();
        fesBonus = new ArrayList<>();
        foodsubsidy = new ArrayList<>();
        taxBear = new ArrayList<>();

        fesBonus.add("Not Applicable");
        fesBonus.add("Regular");
        fesBonus.add("100 % of Basic");
        fesBonus.add("75 % of Basic");
        fesBonus.add("50 % of Basic");
        fesBonus.add("25% of Basic");


        foodsubsidy.add("N/A");
        foodsubsidy.add("Fixed");
        foodsubsidy.add("Subject to attendance");

        taxBear.add("Self");
        taxBear.add("Company");

        getTranscriptTwoData();

        return view;
    }

    public void getTranscriptTwoData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        secondPageData = new ArrayList<>();

        String secondPageUrl = api_url_front+"emp_information/getTranscriptSecondPageData/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest secondDataReq = new StringRequest(Request.Method.GET, secondPageUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empTranscriptSecond = array.getJSONObject(i);

                        String job_exp_date = empTranscriptSecond.getString("job_exp_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_exp_date");
                        job_exp_date = transformText(job_exp_date);

                        String job_actual_date = empTranscriptSecond.getString("job_actual_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_actual_date");
                        job_actual_date = transformText(job_actual_date);

                        String job_calculation_date = empTranscriptSecond.getString("job_calculation_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_calculation_date");
                        job_calculation_date = transformText(job_calculation_date);

                        String job_recruit_type = empTranscriptSecond.getString("job_recruit_type")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_recruit_type");
                        job_recruit_type = transformText(job_recruit_type);

                        String job_increment_flag = empTranscriptSecond.getString("job_increment_flag")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_increment_flag");
                        job_increment_flag = transformText(job_increment_flag);

                        String job_status = empTranscriptSecond.getString("job_status")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_status");
                        job_status = transformText(job_status);

                        String job_status_details = empTranscriptSecond.getString("job_status_details")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_status_details");
                        job_status_details = transformText(job_status_details);

                        String job_probation_period = empTranscriptSecond.getString("job_probation_period")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_probation_period");
                        job_probation_period = transformText(job_probation_period);

                        String job_probation_date = empTranscriptSecond.getString("job_probation_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_probation_date");
                        job_probation_date = transformText(job_probation_date);

                        String job_work_pe_date = empTranscriptSecond.getString("job_work_pe_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_work_pe_date");
                        job_work_pe_date = transformText(job_work_pe_date);

                        String job_work_p_period = empTranscriptSecond.getString("job_work_p_period")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_work_p_period");
                        job_work_p_period = transformText(job_work_p_period);

                        String job_work_p_com_date = empTranscriptSecond.getString("job_work_p_com_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_work_p_com_date");
                        job_work_p_com_date = transformText(job_work_p_com_date);

                        String job_quit_date = empTranscriptSecond.getString("job_quit_date")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_quit_date");
                        job_quit_date = transformText(job_quit_date);

                        String job_shift = empTranscriptSecond.getString("job_shift")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_shift");
                        job_shift = transformText(job_shift);

                        String package_for_emp = empTranscriptSecond.getString("package_for_emp")
                                .equals("null") ? "" : empTranscriptSecond.getString("package_for_emp");
                        package_for_emp = transformText(package_for_emp);

                        String job_increment_amt = empTranscriptSecond.getString("job_increment_amt")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_increment_amt");
                        job_increment_amt = transformText(job_increment_amt);

                        String job_decrement_amt = empTranscriptSecond.getString("job_decrement_amt")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_decrement_amt");
                        job_decrement_amt = transformText(job_decrement_amt);

                        String job_gross_sal = empTranscriptSecond.getString("job_gross_sal")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_gross_sal");
                        job_gross_sal = transformText(job_gross_sal);

                        String job_cash_amt = empTranscriptSecond.getString("job_cash_amt")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_cash_amt");
                        job_cash_amt = transformText(job_cash_amt);

                        String job_basic = empTranscriptSecond.getString("job_basic")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_basic");
                        job_basic = transformText(job_basic);

                        String job_hr = empTranscriptSecond.getString("job_hr")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_hr");
                        job_hr = transformText(job_hr);

                        String job_md = empTranscriptSecond.getString("job_md")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_md");
                        job_md = transformText(job_md);

                        String job_conveyance = empTranscriptSecond.getString("job_conveyance")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_conveyance");
                        job_conveyance = transformText(job_conveyance);

                        String job_habitation = empTranscriptSecond.getString("job_habitation")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_habitation");
                        job_habitation = transformText(job_habitation);

                        String job_utilities = empTranscriptSecond.getString("job_utilities")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_utilities");
                        job_utilities = transformText(job_utilities);

                        String job_house_allowance = empTranscriptSecond.getString("job_house_allowance")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_house_allowance");
                        job_house_allowance = transformText(job_house_allowance);

                        String job_entertainment = empTranscriptSecond.getString("job_entertainment")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_entertainment");
                        job_entertainment = transformText(job_entertainment);

                        String job_factory_allowance_amt = empTranscriptSecond.getString("job_factory_allowance_amt")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_factory_allowance_amt");
                        job_factory_allowance_amt = transformText(job_factory_allowance_amt);

                        String job_committed_salary = empTranscriptSecond.getString("job_committed_salary")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_committed_salary");
                        job_committed_salary = transformText(job_committed_salary);

                        String job_festival_bonus_flag = empTranscriptSecond.getString("job_festival_bonus_flag")
                                .equals("null") ? "0" : empTranscriptSecond.getString("job_festival_bonus_flag");
                        job_festival_bonus_flag = transformText(job_festival_bonus_flag);

                        String job_mobile_bill = empTranscriptSecond.getString("job_mobile_bill")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_mobile_bill");
                        job_mobile_bill = transformText(job_mobile_bill);

                        String job_oth_all = empTranscriptSecond.getString("job_oth_all")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_oth_all");
                        job_oth_all = transformText(job_oth_all);

                        String job_fixed_ot = empTranscriptSecond.getString("job_fixed_ot")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_fixed_ot");
                        job_fixed_ot = transformText(job_fixed_ot);

                        String job_food_subsidy_flag = empTranscriptSecond.getString("job_food_subsidy_flag")
                                .equals("null") ? "0" : empTranscriptSecond.getString("job_food_subsidy_flag");
                        job_food_subsidy_flag = transformText(job_food_subsidy_flag);

                        String job_food_subsidy = empTranscriptSecond.getString("job_food_subsidy")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_food_subsidy");
                        job_food_subsidy = transformText(job_food_subsidy);

                        String job_oth_all_dtl = empTranscriptSecond.getString("job_oth_all_dtl")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_oth_all_dtl");
                        job_oth_all_dtl = transformText(job_oth_all_dtl);

                        String job_overtime_avail_flag = empTranscriptSecond.getString("job_overtime_avail_flag")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_overtime_avail_flag");
                        job_overtime_avail_flag = transformText(job_overtime_avail_flag);

                        String job_ot_amt = empTranscriptSecond.getString("job_ot_amt")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_ot_amt");
                        job_ot_amt = transformText(job_ot_amt);

                        String job_pf_type = empTranscriptSecond.getString("job_pf_type")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_pf_type");
                        job_pf_type = transformText(job_pf_type);

                        String job_pf = empTranscriptSecond.getString("job_pf")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_pf");
                        job_pf = transformText(job_pf);

                        String job_ot_cal_period = empTranscriptSecond.getString("job_ot_cal_period")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_ot_cal_period");
                        job_ot_cal_period = transformText(job_ot_cal_period);

                        String job_tax_flag = empTranscriptSecond.getString("job_tax_flag")
                                .equals("null") ? "0" : empTranscriptSecond.getString("job_tax_flag");
                        job_tax_flag = transformText(job_tax_flag);

                        String job_tax = empTranscriptSecond.getString("job_tax")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_tax");
                        job_tax = transformText(job_tax);

                        String job_sitting = empTranscriptSecond.getString("job_sitting")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_sitting");
                        job_sitting = transformText(job_sitting);

                        String job_lunch_status = empTranscriptSecond.getString("job_lunch_status")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_lunch_status");
                        job_lunch_status = transformText(job_lunch_status);

                        String job_transport = empTranscriptSecond.getString("job_transport")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_transport");
                        job_transport = transformText(job_transport);

                        String job_pabx_corporate = empTranscriptSecond.getString("job_pabx_corporate")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_pabx_corporate");
                        job_pabx_corporate = transformText(job_pabx_corporate);

                        String job_pabx_factory = empTranscriptSecond.getString("job_pabx_factory")
                                .equals("null") ? "" : empTranscriptSecond.getString("job_pabx_factory");
                        job_pabx_factory = transformText(job_pabx_factory);

                        secondPageData.add(new SecondPageData(job_exp_date,job_actual_date,job_calculation_date,
                                job_recruit_type, job_increment_flag,job_status,job_status_details,
                                job_probation_period,job_probation_date,job_work_pe_date,job_work_p_period,
                                job_work_p_com_date,job_quit_date,job_shift,package_for_emp,
                                job_increment_amt,job_decrement_amt,job_gross_sal,job_cash_amt,
                                job_basic,job_hr,job_md,job_conveyance,
                                job_habitation,job_utilities,job_house_allowance,job_entertainment,
                                job_factory_allowance_amt,job_committed_salary,job_festival_bonus_flag,job_mobile_bill,
                                job_oth_all,job_fixed_ot,job_food_subsidy_flag,job_food_subsidy,
                                job_oth_all_dtl,job_overtime_avail_flag,job_ot_amt,job_pf_type,
                                job_pf,job_ot_cal_period,job_tax_flag,job_tax,
                                job_sitting,job_lunch_status,job_transport,job_pabx_corporate,
                                job_pabx_factory));

                    }
                    connected = true;
                }
                else {
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(secondDataReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (secondPageData.size() != 0) {
                    for (int i = 0 ; i < secondPageData.size(); i++) {

                        String dateC = secondPageData.get(i).getExpectedDate();
                        if (dateC != null) {
                            expecJoin.setText(dateC);
                        } else {
                            expecJoin.setText("");
                        }

                        String datt = secondPageData.get(i).getActualDate();
                        if (datt != null) {
                            actuJoin.setText(datt);
                        } else {
                            actuJoin.setText("");
                        }

                        String aptdat = secondPageData.get(i).getCalculationDate();

                        if (aptdat != null) {
                            calcJoin.setText(aptdat);
                        } else {
                            calcJoin.setText("");
                        }

                        String procompdat = secondPageData.get(i).getProba_comp();

                        if (procompdat != null) {
                            probaComp.setText(procompdat);
                        } else {
                            probaComp.setText("");
                        }

                        String woreffedat = secondPageData.get(i).getWork_per_date();

                        if (woreffedat != null) {
                            workPeEdDate.setText(woreffedat);
                        } else {
                            workPeEdDate.setText("");
                        }

                        String workPEEEComDa = secondPageData.get(i).getWork_per_comp();

                        if (workPEEEComDa != null) {
                            workPeComp.setText(workPEEEComDa);
                        } else {
                            workPeComp.setText("");
                        }

                        String quitdAt = secondPageData.get(i).getQuit();

                        if (quitdAt != null) {
                            quitDate.setText(quitdAt);
                        } else {
                            quitDate.setText("");
                        }

                        if (secondPageData.get(i).getAppointmentType() != null) {
                            appoinType.setText(secondPageData.get(i).getAppointmentType());
                        }
                        if (secondPageData.get(i).getRevisionType() != null) {
                            revisoinType.setText(secondPageData.get(i).getRevisionType());
                        }
                        if (secondPageData.get(i).getSer_status() != null) {
                            serviceStatus.setText(secondPageData.get(i).getSer_status());
                        }
                        if (secondPageData.get(i).getDesc() != null) {
                            description.setText(secondPageData.get(i).getDesc());
                        }
                        if (secondPageData.get(i).getProba_per() != null) {
                            probaPeriod.setText(secondPageData.get(i).getProba_per());
                        }
                        if (secondPageData.get(i).getWork_per_period() != null) {
                            workPePeriod.setText(secondPageData.get(i).getWork_per_period());
                        }
                        if (secondPageData.get(i).getShift() != null) {
                            shiftAlloc.setText(secondPageData.get(i).getShift());
                        }


                        if (secondPageData.get(i).getSalary_pack() != null) {
                            salaryPac.setText(secondPageData.get(i).getSalary_pack());
                        }
                        if (secondPageData.get(i).getIncre_amnt() != null) {
                            increAmnt.setText(secondPageData.get(i).getIncre_amnt());
                        }
                        if (secondPageData.get(i).getDecr_amnt() != null) {
                            decreAmnt.setText(secondPageData.get(i).getDecr_amnt());
                        }
                        if (secondPageData.get(i).getGross_sal() != null) {
                            grossSalary.setText(secondPageData.get(i).getGross_sal());
                        }
                        if (secondPageData.get(i).getCash_sal() != null) {
                            cashSalary.setText(secondPageData.get(i).getCash_sal());
                        }
                        if (secondPageData.get(i).getBasic_sal() != null) {
                            basicSalary.setText(secondPageData.get(i).getBasic_sal());
                        }
                        if (secondPageData.get(i).getHouse_rent() != null) {
                            houseRent.setText(secondPageData.get(i).getHouse_rent());
                        }
                        if (secondPageData.get(i).getMed_allow() != null) {
                            medicalAllow.setText(secondPageData.get(i).getMed_allow());
                        }
                        if (secondPageData.get(i).getConv_allow() != null) {
                            conveyaAllow.setText(secondPageData.get(i).getConv_allow());
                        }
                        if (secondPageData.get(i).getHabitation() != null) {
                            habitation.setText(secondPageData.get(i).getHabitation());
                        }
                        if (secondPageData.get(i).getUtilti() != null) {
                            utilty.setText(secondPageData.get(i).getUtilti());
                        }
                        if (secondPageData.get(i).getHoiuse_allow() != null) {
                            houseAllow.setText(secondPageData.get(i).getHoiuse_allow());
                        }
                        if (secondPageData.get(i).getEntertain() != null) {
                            entertain.setText(secondPageData.get(i).getEntertain());
                        }
                        if (secondPageData.get(i).getFact_allow() != null) {
                            factoryAllow.setText(secondPageData.get(i).getFact_allow());
                        }


                        if (secondPageData.get(i).getComm_sal() != null) {
                            committedSalary.setText(secondPageData.get(i).getComm_sal());
                        }
                        if (secondPageData.get(i).getFes_bon_type() != null) {
                            fesTypeBonus.setText(fesBonus.get(Integer.parseInt(secondPageData.get(i).getFes_bon_type())));
                        }
                        if (secondPageData.get(i).getMobile_bill() != null) {
                            mobileBill.setText(secondPageData.get(i).getMobile_bill());
                        }
                        if (secondPageData.get(i).getAdd_allow() != null) {
                            additinAllow.setText(secondPageData.get(i).getAdd_allow());
                        }
                        if (secondPageData.get(i).getFixed_ot() != null) {
                            fixedOt.setText(secondPageData.get(i).getFixed_ot());
                        }
                        if (secondPageData.get(i).getFood_sub_type() != null) {
                            foodSubsidyType.setText(foodsubsidy.get(Integer.parseInt(secondPageData.get(i).getFood_sub_type())));
                        }
                        if (secondPageData.get(i).getFood_sub() != null) {
                            foodSubsidy.setText(secondPageData.get(i).getFood_sub());
                        }
                        if (secondPageData.get(i).getOthers_allow_detl() != null) {
                            otherAlloDetl.setText(secondPageData.get(i).getOthers_allow_detl());
                        }
                        if (secondPageData.get(i).getOver_allow_app() != null) {
                            overtimeAllowApp.setText(secondPageData.get(i).getOver_allow_app());
                        }
                        if (secondPageData.get(i).getOver_rate() != null) {
                            overtimeRate.setText(secondPageData.get(i).getOver_rate());
                        }
                        if (secondPageData.get(i).getPro_fund_type() != null) {
                            providentFundType.setText(secondPageData.get(i).getPro_fund_type());
                        }
                        if (secondPageData.get(i).getPro_fund() != null) {
                            providentFund.setText(secondPageData.get(i).getPro_fund());
                        }
                        if (secondPageData.get(i).getOt_cal_per() != null) {
                            otCalPeriod.setText(secondPageData.get(i).getOt_cal_per());
                        }



                        if (secondPageData.get(i).getTax_bearer() != null) {
                            taxBearer.setText(taxBear.get(Integer.parseInt(secondPageData.get(i).getTax_bearer())));
                        }
                        if (secondPageData.get(i).getTax_deduc() != null) {
                            taxDeduc.setText(secondPageData.get(i).getTax_deduc());
                        }
                        if (secondPageData.get(i).getSitting() != null) {
                            sitting.setText(secondPageData.get(i).getSitting());
                        }
                        if (secondPageData.get(i).getLunch() != null) {
                            lunchStatus.setText(secondPageData.get(i).getLunch());
                        }
                        if (secondPageData.get(i).getTransport() != null) {
                            transport.setText(secondPageData.get(i).getTransport());
                        }
                        if (secondPageData.get(i).getPabx_off() != null) {
                            pabx_office.setText(secondPageData.get(i).getPabx_off());
                        }
                        if (secondPageData.get(i).getPabx_fact() != null) {
                            pabx_factory.setText(secondPageData.get(i).getPabx_fact());
                        }
                    }
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getTranscriptTwoData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    ((Activity)mContext).finish();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getTranscriptTwoData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                ((Activity)mContext).finish();
                dialog.dismiss();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}