package com.example.CalenDR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class triggerDates {

    public static void main() {
        Timer timer = new Timer();
		
        timer.schedule(new TimerTask() {
            public void run() {
                if(LocalTime.now().getHour() == 4) triggerDates(); //would be 0 but server 4 hours ahead
            }
        }, 0, 60*60*1000);
	}

    private static void triggerDates(){
        try{
            Connection connection = dbController.main();

            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM users");
			int rows = 0;
			if (rs.next()) {
                rows = rs.getInt(1);
            }
			int offset = 0;

            

            //loop in batches of 10k
            while(rows >= offset) {
                rs = connection.createStatement().executeQuery("SELECT * FROM users LIMIT 1000 OFFSET " + offset + ";");
                
                //loop through each user
                while(rs.next()) {
                    //send daily 
                    String email = rs.getString("email");
                    String name = rs.getString("name");
                    ArrayList<String> appointmentList = new ArrayList<>(Arrays.asList(rs.getString("appointments").split(",")));
                    ArrayList<String> appointmentNotiList = new ArrayList<>(Arrays.asList(rs.getString("appointmentNotifications").split(",")));

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.ENGLISH);
                    for(int i = 0; i < appointmentNotiList.size(); ++i){
                        if(appointmentNotiList.get(i).equals(LocalDate.now().toString())){
                            String body = "Hey, " + name + "\n\nOn " + LocalDate.parse(appointmentList.get(i*2+1)).format(formatter) + ", you have an appointment for \"" + appointmentList.get(i*2) + "\"\n\nStay healthy, CalenDR.";
                            String from =  "[redacted]";
                            String to = email;
                            String subject = "Appointments for " + LocalDate.parse(appointmentList.get(i*2+1)).format(formatter) + " from CalenDR";
                            emailSender.send(from, to, body, subject);
                        }   
                    }
                    

                    //check if its first day of month, if not prevent from doing monthly emails
                    if(LocalDate.now().getDayOfMonth() != 1) continue;


                    //obtain info
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
                    HashMap<Integer, ArrayList<String>> immunizationTriggerDates = new HashMap<>();

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

                        immunizationTriggerDates.put(34*12, new ArrayList<>());
                        immunizationTriggerDates.get(34*12).add("Td (booster) - Tetanus, diphtheria (Done every 10 years)");
                        
                        immunizationTriggerDates.get(65*12).add("HZ - Herpes Zoster");
                        immunizationTriggerDates.get(65*12).add("Pneu-P-23 - Pneumococcal Polysaccharide 23");
                    }
                    else if(immunizationProgress == 1){
                        //1st visit
                        immunizationTriggerDates.put((int)(age*12), new ArrayList<>());
                        immunizationTriggerDates.get((int)(age*12)).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                        immunizationTriggerDates.get((int)(age*12)).add("Men-C-C - Meningococcal Conjugate C");
        
                        double curAge = age + 0;
                        if(curAge < 4){
                            immunizationTriggerDates.get((int)(age*12)).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                            immunizationTriggerDates.get((int)(age*12)).add("MMR - Measles, Mumps, Rubella");
                        }
                        else if(4 <= curAge && curAge < 5){
                            immunizationTriggerDates.get((int)(age*12)).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                            immunizationTriggerDates.get((int)(age*12)).add("MMRV - Measles, Mumps, Rubella, Varicella");
                        }
                        else{
                            immunizationTriggerDates.get((int)(age*12)).add("MMRV - Measles, Mumps, Rubella, Varicella");
                        }
        
                        //2nd visit
                        immunizationTriggerDates.put((int)(age*12)+2, new ArrayList<>());
                        curAge += (double)2/12;
                        if(curAge < 5){
                            immunizationTriggerDates.get((int)(age*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                            if(curAge - (double)2/12 < 2){
                                immunizationTriggerDates.get((int)(age*12)+2).add("Pneu-C-13 - Pneumococcal Conjugate 13");
                                immunizationTriggerDates.get((int)(age*12)+2).add("Var - Varicella");
                            }
                            else if(2 <= curAge - (double)2/12 && curAge - (double)2/12 < 4){
                                immunizationTriggerDates.get((int)(age*12)+2).add("Var - Varicella");
                            }
                            else{
                                //intentionally empty
                            }
                        }        
                        else if(5 <= curAge && curAge < 7){
                            immunizationTriggerDates.get((int)(age*12)+2).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                        }        
                        else{
                            immunizationTriggerDates.get((int)(age*12)+2).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
                        }
        
                        //3rd visit
                        immunizationTriggerDates.put((int)(age*12)+4, new ArrayList<>());
                        curAge += (double)2/12;
                        if(curAge < 7){
                            immunizationTriggerDates.get((int)(age*12)+4).add("DTaP-IPV-Hib - Diphtheria, Tetanus, Pertussis, Polio, Haemophilus influenzae type b");
                        }
                        else{
                            immunizationTriggerDates.get((int)(age*12)+4).add("Tdap-IPV - Tetanus, diphtheria, pertussis, Polio");
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
                        immunizationTriggerDates.put(34*12, new ArrayList<>());
                        immunizationTriggerDates.get(34*12).add("Td (booster) - Tetanus, diphtheria (Done every 10 years)");

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


                    ArrayList<String> appointments = new ArrayList<>();
                    double curAge = age + (double)monthsPassed/12;
                    if(scheduleImmunizations && immunizationTriggerDates.get((int)(curAge*12)) != null){
                        for(String next : immunizationTriggerDates.get((int)(curAge*12))){
                            appointments.add(next);
                        }
                    }

                    if(scheduleCancer){
                        if(gender == 1) {
                            //cervical cancer
                            if(sexuallyActiveAge != -1 && curAge <= 70) {
                                int monthsTo = (int)(3*12-(curAge-Math.max(sexuallyActiveAge, age))%3*12);
                                if(monthsTo == 0) appointments.add("Pap test");
                            }
                            
                            //breast cancer
                            if(breastCancer && curAge >= 30) {
                                int monthsTo = (int)(12-curAge*12%12);
                                if(monthsTo == 0) appointments.add("Mammogram");
                            }
                            else if(curAge >= 50) {
                                int monthsTo = (int)(2*12-curAge%2*12);
                                if(monthsTo == 0) appointments.add("Mammogram");  
                            }
                        }
                        else {
                            //prostate cancer
                            if(prostateCancer && curAge <= 50) { 
                                int monthsTo = (int)((50-curAge)*12);
                                if(monthsTo == 0) appointments.add("PSA Test");  
                            }
                        }
                        
                        
                        //lung cancer
                        if(lungCancer && timeSmoking + (55-curAge) + (double)monthsPassed/12 >= 20 && timeSmoking != -1) {
                            if(curAge <= 55){
                                int monthsTo = (int)((55-curAge)*12);
                                if(monthsTo == 0) appointments.add("Low-dose CT Scan"); 
                            }
                        }
            
                        //colorectal
                        if(curAge >= 50) {
                            int monthsTo = (int)(5*12-curAge%5*12);
                            if(monthsTo == 0) appointments.add("Colonoscopy"); 
                            
                            monthsTo = (int)(2*12-curAge%2*12);
                            if(monthsTo == 0) appointments.add("Fecal immunochemical test"); 
                        }else{
                            int monthsTo = (int)(50*12-curAge*12);
                            if(monthsTo == 0){
                                appointments.add("Colonoscopy"); 
                                appointments.add("Fecal immunochemical test"); 
                            } 
                        }
                    }


                    if(scheduleInfluenzaShots && LocalDate.now().getMonthValue() == 10){
                        appointments.add("Influenza Shot");
                    }


                    if(!appointments.isEmpty()){
                        StringBuilder strAppointments = new StringBuilder();
                        for(int i = 0; i < appointments.size(); ++i){
                            strAppointments.append((i+1) + ". ");
                            strAppointments.append(appointments.get(i));
                            strAppointments.append("\n");
                        }

                        String body = "Hey, " + name + "\n\nThis month, you have the following appointments recommended:\n\n" + strAppointments + "\nStay healthy, CalenDR.";
                        String from =  "[redacted]";
                        String to = email;
                        String subject = "Appointments for " + dashboard.format(0) + " from CalenDR";
                        emailSender.send(from, to, body, subject);
                    }
                }

                offset += 1000;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
