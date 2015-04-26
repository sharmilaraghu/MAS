/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gayatrisingh
 */
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ScheduledFuture;
import java.io.*;
import java.util.HashMap;
import org.json.*;

public class BeeperControl {
   private final int THREAD_POOL_SIZE = 50; 
   private HashMap<String,SUser> hm = new HashMap<String,SUser>();
   private ReadWriteLock l = new ReentrantReadWriteLock();
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
   
   /*
   // being sent to device
    {
      "salarm_id" : "SAlarm id"
      "update_alarm" : "7:30"
    }
    */
   private class printHashSet implements Runnable{
     String key;
     public printHashSet(SUser user){
         this.key = user.getKey();
     }
     public void run(){
      l.readLock().lock();
      try {
        SUser user = hm.get(key);
        JSONObject obj = new JSONObject();
        obj.put("salarm_id", user.getSid());
        obj.put("update_alarm", user.getAlarm() - 3600);
        obj.put("from", user.getDid());
        (new Wrapper()).send_rpc(obj);            
          // System.out.println(hm.get(key)); 
      } catch(IOException e) {
        e.getStackTrace();
      } finally{
          l.readLock().unlock();
      }
     }
   }
   
   private class writeToHashSet implements Runnable{
     String key;
     SUser user;
     public writeToHashSet(SUser user){
         // this.key = key;
      this.user = user;     
      this.key = user.getKey();
     }
       
     public void run() {
      l.writeLock().lock();
      try {            
          hm.put(key, user); 
      } finally{
          l.writeLock().unlock();
      }           
     }
   }

   public void beepInTenSeconds(SUser user) {
     int key = 10;
     int alarm_time = user.getJson().getInt("alarm_time");
     System.out.println("Time to schedule : " + alarm_time);
     final Runnable scheduleBeep = new writeToHashSet(user);
     final Runnable beeper = new printHashSet(user);
 
     final ScheduledFuture<?> scheduledbeeperHandle = 
      scheduler.schedule(scheduleBeep, 0, SECONDS);       
//         scheduler.scheduleAtFixedRate(scheduleBeep, 0, 2, SECONDS);
     final ScheduledFuture<?> beeperHandle = 
       scheduler.schedule(beeper, 5, SECONDS);
//         scheduler.scheduleAtFixedRate(beeper, 3, 1, SECONDS);
   }
   
   public static void main(String[] args) throws Exception {
       BeeperControl bc = new BeeperControl();
       Wrapper w = new Wrapper();
       while(true){
       	bc.beepInTenSeconds(w.listen_rpc());
       }
       //for(int i = 0;i <10;i++)
       // bc.beepInTenSeconds(i);
   }
 }
