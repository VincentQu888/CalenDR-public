package com.example.CalenDR;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class dashboard {

    @Autowired
	private session session;

    int lim = 1;
    HashMap<Integer, ArrayList<String>> immunizationTriggerDates = new HashMap<>();


    public static String format(int monthsTo){
        LocalDate date = LocalDate.now().plusMonths(monthsTo);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

        return date.format(formatter);
    }


    public static int monthToInt(String month) {
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };

        for(int i = 0; i < months.length; ++i) {
            if(months[i].equalsIgnoreCase(month)) return i; 
        }

        return -1;
    }


	private void fillDates(short immunizationProgress){
        immunizationTriggerDates = new HashMap<>();
        
        try{
            Connection connection = dbController.main();
            String email = ((ResultSet)session.getAttribute("rs")).getString("email");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            double age = rs.getDouble("age");
            int previousVisits = rs.getInt("previousVisits");
            double lastVisit = rs.getDouble("lastVisit"); //time since last visit (years)
            if(previousVisits == 0) lastVisit = 0;

            if(immunizationProgress == 0){
                immunizationTriggerDates.put(2, new ArrayList<>());
                immunizationTriggerDates.put(4, new ArrayList<>());
                immunizationTriggerDates.put(6, new ArrayList<>());
                immunizationTriggerDates.put(12, new ArrayList<>());
                immunizationTriggerDates.put(15, new ArrayList<>());
                immunizationTriggerDates.put(18, new ArrayList<>());
                immunizationTriggerDates.put(4*12, new ArrayList<>());
                immunizationTriggerDates.put(12*12, new ArrayList<>());
                immunizationTriggerDates.put(14*12, new ArrayList<>());
                immunizationTriggerDates.put(24*12, new ArrayList<>());
                immunizationTriggerDates.put(34*12, new ArrayList<>());
                immunizationTriggerDates.put(65*12, new ArrayList<>());

                immunizationTriggerDates.get(2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                immunizationTriggerDates.get(2).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                immunizationTriggerDates.get(2).add("Rot-1 - Rotavirus");

                immunizationTriggerDates.get(4).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                immunizationTriggerDates.get(4).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                immunizationTriggerDates.get(4).add("Rot-1 - Rotavirus");

                immunizationTriggerDates.get(6).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");

                immunizationTriggerDates.get(12).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                immunizationTriggerDates.get(12).add("Men-C-C - Meningococcal Conjugate C");
                immunizationTriggerDates.get(12).add("MMR - Measles, Mumps, Rubella");

                immunizationTriggerDates.get(15).add("Var - Varicella");

                immunizationTriggerDates.get(18).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");

                immunizationTriggerDates.get(4*12).add("MMRV - Measles, Mumps, Rubella, Varicella");
                immunizationTriggerDates.get(4*12).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");

                immunizationTriggerDates.get(12*12).add("HB - Hepatitis B");
                immunizationTriggerDates.get(12*12).add("Men-C-ACYW - Meningcoccal Conjugate ACYW-135");
                immunizationTriggerDates.get(12*12).add("HPV-9 - Human Papillomavirus");

                immunizationTriggerDates.get(14*12).add("Tdap - Tetanus, diphtheria, pertussis");

                immunizationTriggerDates.get(24*12).add("Tdap - Tetanus, diphtheria, pertussis");

                immunizationTriggerDates.get(65*12).add("HZ - Herpes Zoster");
                immunizationTriggerDates.get(65*12).add("Pneu-P-23 - Pneumococcal Polysaccharide 23");
            }
            else if(immunizationProgress == 1){
                double curAge = age + 0; //age after however much time for nth visit
                //1st visit
                if(previousVisits <= 0){
                    immunizationTriggerDates.put((int)(lastVisit*12), new ArrayList<>());
                    immunizationTriggerDates.get((int)(lastVisit*12)).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                    immunizationTriggerDates.get((int)(lastVisit*12)).add("Men-C-C - Meningococcal Conjugate C");  

                    if(curAge < 4){
                        immunizationTriggerDates.get((int)(lastVisit*12)).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                        immunizationTriggerDates.get((int)(lastVisit*12)).add("MMR - Measles, Mumps, Rubella");
                    }
                    else if(4 <= curAge && curAge < 5){
                        immunizationTriggerDates.get((int)(lastVisit*12)).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                        immunizationTriggerDates.get((int)(lastVisit*12)).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    }
                    else{
                        immunizationTriggerDates.get((int)(lastVisit*12)).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    }
                }

                //2nd visit
                if(previousVisits <= 1){
                    immunizationTriggerDates.put((int)(lastVisit*12)+2, new ArrayList<>());
                    curAge += (double)2/12;
                    if(curAge < 5){
                        immunizationTriggerDates.get((int)(lastVisit*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                        if(curAge - (double)2/12 < 2){
                            immunizationTriggerDates.get((int)(lastVisit*12)+2).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                            immunizationTriggerDates.get((int)(lastVisit*12)+2).add("Var - Varicella");
                        }
                        else if(2 <= curAge - (double)2/12 && curAge - (double)2/12 < 4){
                            immunizationTriggerDates.get((int)(lastVisit*12)+2).add("Var - Varicella");
                        }
                        else{
                            //intentionally empty
                        }
                    }        
                    else if(5 <= curAge && curAge < 7){
                        immunizationTriggerDates.get((int)(lastVisit*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                    }        
                    else{
                        immunizationTriggerDates.get((int)(lastVisit*12)+2).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                    }
                }

                //3rd visit
                if(previousVisits <= 2){
                    immunizationTriggerDates.put((int)(age*12)+2, new ArrayList<>());
                    curAge += (double)2/12;
                    if(curAge < 7){
                        immunizationTriggerDates.get((int)(age*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                    }
                    else{
                        immunizationTriggerDates.get((int)(age*12)+2).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                    }
                }

                //4th visit
                immunizationTriggerDates.put((int)(age*12)+10, new ArrayList<>());
                curAge += (double)6/12;
                if(curAge < 4){
                    immunizationTriggerDates.get((int)(age*12)+10).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                }
                else{
                    immunizationTriggerDates.get((int)(age*12)+10).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    immunizationTriggerDates.get((int)(age*12)+10).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                }

                //5th visit
                if(curAge < 4){
                    immunizationTriggerDates.put((int)(age*12)+16, new ArrayList<>());
                    immunizationTriggerDates.get((int)(age*12)+16).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    immunizationTriggerDates.get((int)(age*12)+16).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                }


                //6th visit
                immunizationTriggerDates.put(12*12, new ArrayList<>());
                immunizationTriggerDates.get(12*12).add("HB - Hepatitis B");
                immunizationTriggerDates.get(12*12).add("Men-C-ACYW - Meningcoccal Conjugate ACYW-135");
                immunizationTriggerDates.get(12*12).add("HPV-9 - Human Papillomavirus");

                //7th visit
                immunizationTriggerDates.put(14*12, new ArrayList<>());
                immunizationTriggerDates.get(14*12).add("Tdap - Tetanus, diphtheria, pertussis");

                //8th visit
                immunizationTriggerDates.put(24*12, new ArrayList<>());
                immunizationTriggerDates.get(24*12).add("If user was <18 years old at previous visit: Tdap - Tetanus, diphtheria, pertussis");
                immunizationTriggerDates.get(24*12).add("If user was >18 years old at previous visit: Td (booster) - Tetanus, diphtheria");

                //rest of trigger dates
                immunizationTriggerDates.put(65*12, new ArrayList<>());
                immunizationTriggerDates.get(65*12).add("HZ - Herpes Zoster");
                immunizationTriggerDates.get(65*12).add("Pneu-P-23 - Pneumococcal Polysaccharide 23");
            }
            else if(immunizationProgress == 2){
                //1st visit
                immunizationTriggerDates.put((int)(age*12), new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                double curAge = age + 0; 

                if(curAge < 13){
                    immunizationTriggerDates.get((int)(age*12)).add("MMR - Measles, Mumps, Rubella");
                    immunizationTriggerDates.get((int)(age*12)).add("Men-C-C - Meningococcal Conjugate C");
                }
                else{
                    immunizationTriggerDates.get((int)(age*12)).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    immunizationTriggerDates.get((int)(age*12)).add("Var - Varicella");
                }

                //2nd visit
                curAge += (double)2/12;
                immunizationTriggerDates.put((int)(age*12)+2, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                if(curAge < 13){
                    immunizationTriggerDates.get((int)(age*12)+2).add("MMR - Measles, Mumps, Rubella");
                }
                else{
                    immunizationTriggerDates.get((int)(age*12)+2).add("MMRV - Measles, Mumps, Rubella, Varicella");
                    immunizationTriggerDates.get((int)(age*12)+2).add("Var - Varicella");
                }

                //3rd visit
                immunizationTriggerDates.put((int)(age*12)+8, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+8).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");

                //4th visit
                immunizationTriggerDates.put((int)(age*12)+8 + 10*12, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+8 + 10*12).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");

                //rest of trigger dates
                immunizationTriggerDates.put((int)(age*12)+8 + 20*12, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+8 + 20*12).add("If user was <18 at previous visit: Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                for(int i = (int)(age*12)+8 + 20*12; i < 1000*12; i += 10*12){
                    immunizationTriggerDates.put(i, new ArrayList<>());
                    immunizationTriggerDates.get(i).add("Td (booster) - Tetanus, diphtheria (Every 10 years)");
                }

                immunizationTriggerDates.put(65*12, new ArrayList<>());
                immunizationTriggerDates.get(65*12).add("HZ - Herpes Zoster");
                immunizationTriggerDates.get(65*12).add("Pneu-P-23 - Pneumococcal Polysaccharide 23");

                if(12 <= age && age <= 18){
                    immunizationTriggerDates.get((int)(age*12)).add("If user has not already completed: HB - Hepatitis B");
                    immunizationTriggerDates.get((int)(age*12)).add("If user has not already completed: Men-C-ACYW - Meningcoccal Conjugate ACYW-135");
                    immunizationTriggerDates.get((int)(age*12)).add("If user has not already completed: HPV-9 - Human Papillomavirus");
                }else{
                    immunizationTriggerDates.put(12*12, new ArrayList<>());
                    immunizationTriggerDates.get(12*12).add("HB - Hepatitis B");
                    immunizationTriggerDates.get(12*12).add("Men-C-ACYW - Meningcoccal Conjugate ACYW-135");
                    immunizationTriggerDates.get(12*12).add("HPV-9 - Human Papillomavirus");
                }
            }
            else if(immunizationProgress == 3){
                //1st visit
                immunizationTriggerDates.put((int)(age*12), new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                immunizationTriggerDates.get((int)(age*12)).add("MMR - Measles, Mumps, Rubella");
                double curAge = age + 0; 

                if(27 <= age && age < 38){
                    immunizationTriggerDates.get((int)(age*12)).add("Men-C-C - Meningococcal Conjugate C");
                }
                else if(24 <= age && age <= 27){
                    immunizationTriggerDates.get((int)(age*12)).add("Men-C-C - Meningococcal Conjugate C");
                }
                else{
                    immunizationTriggerDates.get((int)(age*12)).add("Var - Varicella");
                    immunizationTriggerDates.get((int)(age*12)).add("Men-C-C - Meningococcal Conjugate C");
                }

                //2nd visit
                curAge += (double)2/12;
                immunizationTriggerDates.put((int)(age*12)+2, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+2).add("Td (booster) - Tetanus, diphtheria");
                immunizationTriggerDates.get((int)(age*12)+2).add("IPV - Polio");
                if(20 <= curAge && curAge < 26){
                    immunizationTriggerDates.get((int)(age*12)+2).add("MMR - Measles, Mumps, Rubella");
                }
                else{
                    immunizationTriggerDates.get((int)(age*12)+2).add("MMR - Measles, Mumps, Rubella");
                    immunizationTriggerDates.get((int)(age*12)+2).add("Var - Varicella");
                }

                //rest of trigger dates
                immunizationTriggerDates.put((int)(age*12)+8, new ArrayList<>());
                immunizationTriggerDates.get((int)(age*12)+8).add("Td (booster) - Tetanus, diphtheria");
                immunizationTriggerDates.get((int)(age*12)+8).add("IPV - Polio");

                for(int i = (int)(age*12)+8 + 10*12; i < 1000*12; i += 10*12){
                    immunizationTriggerDates.put(i, new ArrayList<>());
                    immunizationTriggerDates.get(i).add("Td (booster) - Tetanus, diphtheria (Every 10 years)");
                }

                immunizationTriggerDates.put(65*12, new ArrayList<>());
                immunizationTriggerDates.get(65*12).add("HZ - Herpes Zoster");
                immunizationTriggerDates.get(65*12).add("Pneu-P-23 - Pneumococcal Polysaccharide 23");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
	}


    @GetMapping("dashboard")
    private String rangeController(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes)throws IOException, InterruptedException{
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @GetMapping("default")
    private String defaultDashboard(Model model)throws IOException, InterruptedException{
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    public String infoRange(String range,  Model model)throws IOException, InterruptedException {
        switch(range){
            case "1":
                lim = 6;
                break;
            case "2":
                lim = 12;
                break;
            case "3":
                lim = 5*12;
                break;
            case "4":
                lim = 10*12;
                break;
            case "5":
                lim = 200*12; //arbitrary large number
                break;
            default:
                break;
        }

        try{
            model = dashboardInfo(model);
        }
        catch(SQLException e){
            e.printStackTrace();

            HashMap<String, ArrayList<String>> appointments = new HashMap<>();
            HashMap<String, ArrayList<String>> screenings = new HashMap<>();
            ArrayList<String> appointmentKeys = new ArrayList<>(); 
            ArrayList<String> screeningKeys = new ArrayList<>();
            ArrayList<String> missed = new ArrayList<>();

            appointments.put("Oops, database error!", new ArrayList<>());
            appointments.get("Oops, database error!").add("Database error. Please try again later.");
            screenings.put("Oops, database error!", new ArrayList<>());
            screenings.get("Oops, database error!").add("Database error. Please try again later.");
            appointmentKeys.add("Oops, database error!");
            screeningKeys.add("Oops, database error!");
            missed.add("Oops, database error!");

            model.addAttribute("appointments", appointments);
            model.addAttribute("screenings", screenings);
            model.addAttribute("missed", missed);
            model.addAttribute("appointmentKeys", appointmentKeys);
            model.addAttribute("screeningKeys", screeningKeys);
            model.addAttribute("name", "John Doe");
        }

        model.addAttribute("range", range);
        return "dashboard/dashboard";
    } 
    
    private Model dashboardInfo(Model model)throws SQLException{
        Connection connection = dbController.main();
        String email = ((ResultSet)session.getAttribute("rs")).getString("email");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
		preparedStatement.setString(1, email);
        ResultSet rs = preparedStatement.executeQuery();

        HashMap<String, ArrayList<String>> appointments = new HashMap<>();
        HashMap<String, ArrayList<String>> screenings = new HashMap<>();
        ArrayList<String> missed = new ArrayList<>();

        rs.next(); 
        

        //obtain info
        String name = rs.getString("name");
        short gender = rs.getShort("gender");
        double age = rs.getDouble("age");
        boolean breastCancer = rs.getBoolean("breastCancer"); //family history of whatever cancer
        boolean lungCancer = rs.getBoolean("lungCancer");
        boolean prostateCancer = rs.getBoolean("prostateCancer");
        short immunizationProgress = rs.getShort("immunizationProgress");
        boolean scheduleImmunizations = rs.getBoolean("scheduleImmunizations");
        boolean scheduleCancer = rs.getBoolean("scheduleCancer");
        boolean scheduleInfluenzaShots = rs.getBoolean("scheduleInfluenzaShots");
        LocalDate signUpDate = rs.getDate("signUpDate").toLocalDate();
        short timeSmoking = rs.getShort("timeSmoking");
        short sexuallyActiveAge = rs.getShort("sexuallyActiveAge");
        
        int monthsPassed = (signUpDate.getMonthValue() - LocalDate.now().getMonthValue()) + (LocalDate.now().getYear()-signUpDate.getYear())*12;
        double curAge = age + (double)monthsPassed/12;
        
        
        if(scheduleImmunizations) { //immunizations
            fillDates(immunizationProgress);

            //normal immunizations
            boolean td = false; //if td booster has already been displayed
            for(int i = (int)(age*12) + monthsPassed; i < age*12 + monthsPassed + lim; ++i){
                if(immunizationTriggerDates.containsKey(i)){
                    int monthsTo = i - (int)(curAge*12);
                    String formatted = format(monthsTo);
                    appointments.put(formatted, new ArrayList<>());
                    
                    for(String next : immunizationTriggerDates.get(i)){
                        if(!next.equals("Td (booster) - Tetanus, diphtheria (Every 10 years)")){
                            appointments.get(formatted).add(next);
                        }
                        else if(!td){
                            appointments.get(formatted).add(next);
                            td = true;
                        }
                        else{
                            appointments.remove(formatted);
                        }
                    }
                }
            }


            //missed
            if(immunizationProgress == 1){
                
            }
            

            //td booster
            if(immunizationProgress == 0 || immunizationProgress == 1){
                if(age <= 34){
                    if(34*12 - age*12 <= lim){
                        int monthsTo = 34*12 - (int)(age*12);
                        String formatted = format(monthsTo);
                        appointments.put(formatted, new ArrayList<>());
                        appointments.get(formatted).add("Td (booster) - Tetanus, diphtheria (Every 10 years)");
                    }
                }
                else{
                    if(10*12 - ((age*12 + monthsPassed) - 34*12)%(10*12) <= lim){
                        int monthsTo = (int)(10*12 - ((age*12 + monthsPassed) - 34*12)%(10*12));
                        String formatted = format(monthsTo);
                        appointments.put(formatted, new ArrayList<>());
                        appointments.get(formatted).add("Td (booster) - Tetanus, diphtheria (Every 10 years)");
                    }
                }
            }
        }

        if(scheduleInfluenzaShots){
            int monthsTo = (12+10 - LocalDate.now().getMonthValue())%12;

            if(monthsTo <= lim && curAge*12 + monthsTo >= 6){
                String formatted = format(monthsTo);
                if(!appointments.containsKey(formatted)) appointments.put(formatted, new ArrayList<>());
                
                appointments.get(formatted).add("Influenza Shot");
            }else if(monthsTo <= lim){
                String formatted = format(monthsTo+12);
                if(!appointments.containsKey(formatted)) appointments.put(formatted, new ArrayList<>());
                
                appointments.get(formatted).add("Influenza Shot");
            }
        }
        
        
        if(scheduleCancer) { //cancer screenings
            if(gender == 1) {
                //cervical cancer
                if(sexuallyActiveAge != -1 && curAge <= 70) {
                    int monthsTo = (int)(3*12-(curAge-Math.max(sexuallyActiveAge, age))%3*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Pap test");
                }
                
                //breast cancer
                if(breastCancer && curAge >= 30) {
                    int monthsTo = (int)(12-curAge*12%12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Mammogram");
                }
                else if(breastCancer){
                    int monthsTo = (int)((30-curAge)*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Mammogram");
                }
                else if(curAge >= 50) {
                    int monthsTo = (int)(2*12-curAge%2*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Mammogram");  
                }
                else{
                    int monthsTo = (int)((50-curAge)*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Mammogram");  
                }
            }
            else {
                //prostate cancer
                if(prostateCancer && curAge <= 50) { 
                    int monthsTo = (int)((50-curAge)*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("PSA Test");  
                }
                else if(prostateCancer && age > 50){
                    missed.add("PSA Test");
                }
            }
            
            
            //lung cancer
            if(lungCancer && timeSmoking + (55-curAge) + (double)monthsPassed/12 >= 20 && timeSmoking != -1) {
                if(curAge <= 55){
                    int monthsTo = (int)((55-curAge)*12);

                    if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                    screenings.get(format(monthsTo)).add("Low-dose CT Scan"); 
                }
                else if(age > 55){
                    missed.add("Low-dose CT Scan");
                }
            }

            //colorectal
            if(curAge >= 50) {
                int monthsTo = (int)(5*12-curAge%5*12);
                if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                screenings.get(format(monthsTo)).add("Colonoscopy"); 
                
                monthsTo = (int)(2*12-curAge%2*12);
                if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                screenings.get(format(monthsTo)).add("Fecal immunochemical test"); 
            }else{
                int monthsTo = (int)(50*12-curAge*12);
                if(!screenings.containsKey(format(monthsTo))) screenings.put(format(monthsTo), new ArrayList<>());
                screenings.get(format(monthsTo)).add("Colonoscopy"); 
                screenings.get(format(monthsTo)).add("Fecal immunochemical test"); 
            }
        }


        ArrayList<String> appointmentKeys = new ArrayList<>(); 
        ArrayList<String> screeningKeys = new ArrayList<>();

        for(String next : appointments.keySet()){
            appointmentKeys.add(next);
        }
        for(String next : screenings.keySet()){
            screeningKeys.add(next);
        } 
        
        Comparator<String> yearMonthComparator = (s1, s2) -> {
            String[] parts1 = s1.split(" ");
            String[] parts2 = s2.split(" ");

            int year1 = Integer.parseInt(parts1[1]);
            int year2 = Integer.parseInt(parts2[1]);

            int month1 = monthToInt(parts1[0]);
            int month2 = monthToInt(parts2[0]);

            if (year1 == year2) {
                return Integer.compare(month1, month2);
            } else {
                return Integer.compare(year1, year2);
            }
        };
        Collections.sort(appointmentKeys, yearMonthComparator); 
        Collections.sort(screeningKeys, yearMonthComparator);


        switch(rs.getShort("missed")){
            case 0:
                break;

            case 1:
                for(int i = 0; i < missed.size(); ++i){
                    if(missed.get(i).equals("PSA Test")) missed.remove(i);
                }
                break;

            case 2:
                for(int i = 0; i < missed.size(); ++i){
                    if(missed.get(i).equals("Low-dose CT Scan")) missed.remove(i);
                }
                break;
            
            case 3:
                missed = new ArrayList<>();
                break;

            default:
                break;
        }



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.ENGLISH);
        String[] appointmentList = rs.getString("appointments").split(",");
        String[] notificationDates = rs.getString("appointmentNotifications").split(",");
        HashMap<String, ArrayList<String>> appointmentAppointments = new HashMap<>();
        ArrayList<String> appointmentAppointmentKeys = new ArrayList<>();
        ArrayList<String> appointmentKeyDates = new ArrayList<>();

        for(int i = 1; i < appointmentList.length; i+=2){
            if(!appointmentAppointments.containsKey(appointmentList[i])){
                appointmentAppointments.put(appointmentList[i], new ArrayList<>());
                appointmentAppointmentKeys.add(appointmentList[i]);
            } 
            appointmentAppointments.get(appointmentList[i]).add(appointmentList[i-1]);
        }

        Comparator<String> timeComparator = (s1, s2) -> {
            String[] parts1 = s1.split("-");
            String[] parts2 = s2.split("-");

            int year1 = Integer.parseInt(parts1[0]);
            int year2 = Integer.parseInt(parts2[0]);

            int month1 = Integer.parseInt(parts1[1]);
            int month2 = Integer.parseInt(parts2[1]);

            int day1 = Integer.parseInt(parts1[2]);
            int day2 = Integer.parseInt(parts2[2]);

            if(year1 != year2) {
                return Integer.compare(year1, year2);
            } else if(month1 != month2){
                return Integer.compare(month1, month2);
            }else{
                return Integer.compare(day1, day2);
            }
        };
        Collections.sort(appointmentAppointmentKeys, timeComparator); 
        for(String next : appointmentAppointmentKeys) appointmentKeyDates.add(LocalDate.parse(next).format(formatter));

        model.addAttribute("appointmentAppointments", appointmentAppointments);
        model.addAttribute("appointmentAppointmentKeys", appointmentAppointmentKeys);
        model.addAttribute("appointmentKeyDates", appointmentKeyDates);
        model.addAttribute("notificationDates", notificationDates);
        model.addAttribute("appointments", appointments);
        model.addAttribute("screenings", screenings);
        model.addAttribute("missed", missed);
        model.addAttribute("appointmentKeys", appointmentKeys);
        model.addAttribute("screeningKeys", screeningKeys);
        model.addAttribute("name", name);
        model.addAttribute("scheduleImmunizations", scheduleImmunizations);
        model.addAttribute("scheduleCancer", scheduleCancer);
        return model;
    }


    @GetMapping("dashboard/settings")
    private String settings(Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException, SQLException{
        // Hidden for public source release: authenticated session access.
        ResultSet rs = (ResultSet)session.getAttribute("rs");


        try{
            //obtain info
            model.addAttribute("name", rs.getString("name"));
            model.addAttribute("email", rs.getString("email"));
            model.addAttribute("gender", rs.getShort("gender"));
            model.addAttribute("age", (int)rs.getDouble("age"));
            model.addAttribute("country", rs.getString("countryOfResidence"));
            model.addAttribute("notificationPreference", rs.getShort("notificationPreference"));
            model.addAttribute("immunizationProgress", rs.getShort("immunizationProgress"));
            model.addAttribute("signUpDate", rs.getDate("signUpDate"));
            model.addAttribute("sexuallyActiveAge", rs.getShort("sexuallyActiveAge"));
            model.addAttribute("yearsSmoking", rs.getShort("timeSmoking"));

            ArrayList<String> risk = new ArrayList<>();
            if(rs.getBoolean("breastCancer")) risk.add("Breast Cancer");
            if(rs.getBoolean("lungCancer")) risk.add("Lung Cancer");
            if(rs.getBoolean("prostateCancer")) risk.add("Prostate Cancer");
            if(risk.isEmpty()) risk.add("None.");
            model.addAttribute("risk", risk);

            ArrayList<String> schedule = new ArrayList<>();
            if(rs.getBoolean("scheduleImmunizations")) schedule.add("Immunizations");
            if(rs.getBoolean("scheduleCancer")) schedule.add("Cancer Screenings");
            if(rs.getBoolean("scheduleInfluenzaShots")) schedule.add("Influenza Shots");
            model.addAttribute("schedule", schedule);
            
            LocalDate signUpDate = rs.getDate("signUpDate").toLocalDate();
            int monthsPassed = (signUpDate.getMonthValue() - LocalDate.now().getMonthValue()) + (LocalDate.now().getYear()-signUpDate.getYear())*12;
            model.addAttribute("curAge", (int)(rs.getDouble("age") + (double)monthsPassed/12));    
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }

        return "dashboard/settings";
    }


    //pair object for reutrning index in appointments
    public static class pair{
        String appointment;
        int index;
        
        public pair(String appointment, int index){
            this.appointment = appointment;
            this.index = index;
        }

        public String getAppointment() {
            return appointment;
        }

        public int getIndex() {
            return index;
        }
    }

    @GetMapping("dashboard/appointments")
    private String appointments(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
        // Hidden for public source release: authenticated session access.
        ResultSet rs = (ResultSet)session.getAttribute("rs");


        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //obtain info
            String[] appointmentList = rs.getString("appointments").split(",");
            String[] notificationDates = rs.getString("appointmentNotifications").split(",");
            HashMap<String, ArrayList<pair>> appointments = new HashMap<>();
            ArrayList<String> appointmentKeys = new ArrayList<>();
            ArrayList<LocalDate> appointmentKeyDates = new ArrayList<>();

            for(int i = 1; i < appointmentList.length; i+=2){
                if(!appointments.containsKey(appointmentList[i])){
                    appointments.put(appointmentList[i], new ArrayList<>());
                    appointmentKeys.add(appointmentList[i]);
                } 
                appointments.get(appointmentList[i]).add(new pair(appointmentList[i-1], (i-1)/2));
            }

            Comparator<String> timeComparator = (s1, s2) -> {
                String[] parts1 = s1.split("-");
                String[] parts2 = s2.split("-");
    
                int year1 = Integer.parseInt(parts1[0]);
                int year2 = Integer.parseInt(parts2[0]);
    
                int month1 = Integer.parseInt(parts1[1]);
                int month2 = Integer.parseInt(parts2[1]);

                int day1 = Integer.parseInt(parts1[2]);
                int day2 = Integer.parseInt(parts2[2]);
    
                if(year1 != year2) {
                    return Integer.compare(year1, year2);
                } else if(month1 != month2){
                    return Integer.compare(month1, month2);
                }else{
                    return Integer.compare(day1, day2);
                }
            };
            Collections.sort(appointmentKeys, timeComparator); 
            for(String next : appointmentKeys) appointmentKeyDates.add(LocalDate.parse(next, formatter));

            model.addAttribute("appointments", appointments);
            model.addAttribute("appointmentKeys", appointmentKeys);
            model.addAttribute("appointmentKeyDates", appointmentKeyDates);
            model.addAttribute("notificationDates", notificationDates);
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }
        

        return "dashboard/appointments";
    }


    @PostMapping("/remove")
    private String remove(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        String item = request.getParameter("missedAppointment");
        Connection connection = dbController.main();

        try{
            if(item.contains("PSA Test")){
                if(rs.getShort("missed") == 0){
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET missed = 1 WHERE email = ?");
			        preparedStatement.setString(1, rs.getString("email"));
                    preparedStatement.execute();
                }
                if(rs.getShort("missed") == 2){
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET missed = 3 WHERE email = ?");
			        preparedStatement.setString(1, rs.getString("email"));
                    preparedStatement.execute();
                }
            }
            else if(item.contains("Low-dose CT Scan")){
                if(rs.getShort("missed") == 0){
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET missed = 2 WHERE email = ?");
			        preparedStatement.setString(1, rs.getString("email"));
                    preparedStatement.execute();
                }
                if(rs.getShort("missed") == 1){
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET missed = 3 WHERE email = ?");
			        preparedStatement.setString(1, rs.getString("email"));
                    preparedStatement.execute();
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }

        return "redirect:/default";
    }


    @PostMapping("/reset")
    private String reset(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        Connection connection = dbController.main(); 

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET missed = 0 WHERE email = ?");
			preparedStatement.setString(1, rs.getString("email"));
            preparedStatement.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }
        
        return "redirect:/default";
    }



    //settings
    @PostMapping("/save")
    private String save(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, SQLException {
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        Connection connection = dbController.main(); 

        try{
            if(request.getParameter("input-info").equals("password")){
                // Hidden for public source release.
                throw new UnsupportedOperationException("Security implementation omitted from public source.");
            }
            else if(request.getParameter("input-info").equals("familyRisks")){
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET breastCancer = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("breast-cancer-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();

                preparedStatement = connection.prepareStatement("UPDATE users SET lungCancer = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("lung-cancer-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();

                preparedStatement = connection.prepareStatement("UPDATE users SET prostateCancer = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("prostate-cancer-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();


                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
                preparedStatement.setString(1, rs.getString("email"));
                rs = preparedStatement.executeQuery();
                rs.next();
                
                session.setAttribute("rs", rs);
            }
            else if(request.getParameter("input-info").equals("schedule")){
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET scheduleImmunizations = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("immunization-schedule-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();

                preparedStatement = connection.prepareStatement("UPDATE users SET scheduleCancer = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("cancer-schedule-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();

                preparedStatement = connection.prepareStatement("UPDATE users SET scheduleInfluenzaShots = ? WHERE email = ?");
                preparedStatement.setInt(1, request.getParameter("influenza-schedule-check-box") != null ? 1 : 0);
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();


                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
                preparedStatement.setString(1, rs.getString("email"));
                rs = preparedStatement.executeQuery();
                rs.next();
                
                session.setAttribute("rs", rs);
            }
            else if(request.getParameter("input-info").equals("delete")){
                // Hidden for public source release.
                throw new UnsupportedOperationException("Security implementation omitted from public source.");
            }
            else {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET " + request.getParameter("input-info") + " = ? WHERE email = ?");
                preparedStatement.setString(1, request.getParameter("input"));
                preparedStatement.setString(2, rs.getString("email"));
                preparedStatement.execute();

                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
                preparedStatement.setString(1, rs.getString("email"));
                rs = preparedStatement.executeQuery();
                rs.next();
                
                session.setAttribute("rs", rs);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }

        return "redirect:/dashboard/settings";
    }

    @PostMapping("saveAppointment")
    private String saveAppointment(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, SQLException {
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        Connection connection = dbController.main(); 
        try{
            String type = request.getParameter("add-appointment-type");
            String appointmentList = rs.getString("appointments").trim();
            String appointmentNotiList = rs.getString("appointmentNotifications").trim();
            if(appointmentList.split(",").length/2 >= 20){
                if(!redirectAttributes.containsAttribute("notification")) redirectAttributes.addFlashAttribute("notification" , "List limit of 20 exceeded.");
                return "redirect:/dashboard/appointments";
            }

            switch(type){
                case "custom":
                    appointmentList += "," + request.getParameter("custom-title") + "," + request.getParameter("date-picker");
                    break;
                case "immunization":
                    appointmentList += "," + request.getParameter("immunization-type") + "," + request.getParameter("date-picker");
                    break;
                case "cancer":
                    appointmentList += "," + request.getParameter("cancer-type") + "," + request.getParameter("date-picker");
                    break;
                case "influenza":
                    appointmentList += ",Influenza Shot," + request.getParameter("date-picker");
                    break;
                default:
                    if(!redirectAttributes.containsAttribute("notification")) redirectAttributes.addFlashAttribute("notification" , "Invalid type of appointment.");
                    return "redirect:/dashboard/appointments";
            }
            if(appointmentList.charAt(0) == ','){
                appointmentList = appointmentList.substring(1, appointmentList.length());
            }
        
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET appointments = ? WHERE email = ?");
            preparedStatement.setString(1, appointmentList);
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();


            appointmentNotiList += "," + request.getParameter("notify-date");
            if(appointmentNotiList.charAt(0) == ','){
                appointmentNotiList = appointmentNotiList.substring(1, appointmentNotiList.length());
            }

            preparedStatement = connection.prepareStatement("UPDATE users SET appointmentNotifications = ? WHERE email = ?");
            preparedStatement.setString(1, appointmentNotiList);
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();


            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, rs.getString("email"));
            rs = preparedStatement.executeQuery();
            rs.next();
            
            session.setAttribute("rs", rs);
            return "redirect:/dashboard/appointments";
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("editAppointment")
    private String editAppointment(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, SQLException {
        deleteAppointment(request, response, model, redirectAttributes);
        
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        Connection connection = dbController.main(); 
        try{
            String type = request.getParameter("edit-appointment-type");
            String appointmentList = rs.getString("appointments");
            String appointmentNotiList = rs.getString("appointmentNotifications").trim();

            switch(type){
                case "custom":
                    appointmentList += "," + request.getParameter("edit-custom-title") + "," + request.getParameter("edit-date-picker");
                    break;
                case "immunization":
                    appointmentList += "," + request.getParameter("edit-immunization-type") + "," + request.getParameter("edit-date-picker");
                    break;
                case "cancer":
                    appointmentList += "," + request.getParameter("edit-cancer-type") + "," + request.getParameter("edit-date-picker");
                    break;
                case "influenza":
                    appointmentList += ",Influenza Shot," + request.getParameter("edit-date-picker");
                    break;
                default:
                    if(!redirectAttributes.containsAttribute("notification")) redirectAttributes.addFlashAttribute("notification" , "Invalid type of appointment.");
                    return "redirect:/dashboard/appointments";
            }
            if(appointmentList.charAt(0) == ','){
                appointmentList = appointmentList.substring(1, appointmentList.length());
            }
        
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET appointments = ? WHERE email = ?");
            preparedStatement.setString(1, appointmentList);
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();


            appointmentNotiList += "," + request.getParameter("edit-notify-date");
            if(appointmentNotiList.charAt(0) == ','){
                appointmentNotiList = appointmentNotiList.substring(1, appointmentNotiList.length());
            }

            preparedStatement = connection.prepareStatement("UPDATE users SET appointmentNotifications = ? WHERE email = ?");
            preparedStatement.setString(1, appointmentNotiList);
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();


            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, rs.getString("email"));
            rs = preparedStatement.executeQuery();
            rs.next();
            
            session.setAttribute("rs", rs);
            return "redirect:/dashboard/appointments";
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PostMapping("deleteAppointment")
    private String deleteAppointment(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws IOException, InterruptedException, SQLException {
        ResultSet rs = (ResultSet)session.getAttribute("rs"); 
        Connection connection = dbController.main(); 
        try{
            String date = request.getParameter("edit-info-date");
            int index = Integer.parseInt(request.getParameter("edit-info-index"));
            ArrayList<String> appointmentList = new ArrayList<>(Arrays.asList(rs.getString("appointments").split(",")));
            ArrayList<String> appointmentNotiList = new ArrayList<>(Arrays.asList(rs.getString("appointmentNotifications").split(",")));

            boolean found = false;
            for(int i = 1, counter = 0; i < appointmentList.size(); i+=2){
                if(appointmentList.get(i).equals(date)){
                    if(counter == index){
                        appointmentList.remove(i-1);
                        appointmentList.remove(i-1); //removes same thing twice cause once you remove first one it shifts everything down
                        appointmentNotiList.remove((i-1)/2);
                        found = true;
                        break;
                    }

                    ++counter;
                }
            }
            if(!found){
                if(!redirectAttributes.containsAttribute("notification")) redirectAttributes.addFlashAttribute("notification" , "Appointment not found.");
                return "redirect:/dashboard/appointments";
            }

            StringBuilder strAppointmentList = new StringBuilder();
            StringBuilder strAppointmentNotiList = new StringBuilder();
            for(String next : appointmentList){
                strAppointmentList.append(next);
                strAppointmentList.append(",");
            }
            if(strAppointmentList.length() > 0) strAppointmentList.deleteCharAt(strAppointmentList.length()-1);

            for(String next : appointmentNotiList){
                strAppointmentNotiList.append(next);
                strAppointmentNotiList.append(",");
            }
            if(strAppointmentNotiList.length() > 0) strAppointmentNotiList.deleteCharAt(strAppointmentNotiList.length()-1);
        

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET appointments = ? WHERE email = ?");
            preparedStatement.setString(1, strAppointmentList.toString());
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement("UPDATE users SET appointmentNotifications = ? WHERE email = ?");
            preparedStatement.setString(1, strAppointmentNotiList.toString());
            preparedStatement.setString(2, rs.getString("email"));
            preparedStatement.execute();


            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, rs.getString("email"));
            rs = preparedStatement.executeQuery();
            rs.next();
            
            session.setAttribute("rs", rs);
            return "redirect:/dashboard/appointments";
        }
        catch(SQLException e){
            e.printStackTrace();
            return "redirect:/error";
        }
    }



}
