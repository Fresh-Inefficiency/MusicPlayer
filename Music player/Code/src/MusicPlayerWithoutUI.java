import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;


public class MusicPlayerWithoutUI {

    Clip clip;
    long currentFrame;
    static String filePath ="D:\\Coding\\Music player\\wav files";
    String status;

    private static String[] playList(){
        filePath ="D:\\Coding\\Music player\\wav files";
        File songs = new File(filePath);
        String[] songList = songs.list();
        return songList;
    }

    private static void printSongsList(){
        String[] songsList = playList();
        int songNo = 1;
        for (String songs : songsList) {
            System.out.println(songNo+". "+songs.substring(0,songs.lastIndexOf(".")));
            songNo++;
        }
    }

    private static File getOneSong(){
        File song = new File(filePath);
        return song;
    }

    private MusicPlayerWithoutUI() throws  Exception{
        AudioInputStream song = AudioSystem.getAudioInputStream(getOneSong());
        clip = AudioSystem.getClip();
        clip.open(song);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void play(){
        clip.start();
        status = "play";
    }

    private void pause(){
        if(status.equals("paused")){
            System.out.println("Song already paused");
            return;
        }
        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    public void resume() throws Exception{
        if(status.equals("play")){
            System.out.println("Song is already being played");
            return;
        }
        clip.setMicrosecondPosition(this.currentFrame);
        this.play();
    }

    public void restart() {
        currentFrame = 0L;
        clip.setFramePosition(0);
        this.play();
    }

    public static void playerFunctions(String song){
        try
        {
            filePath = filePath+"\\"+song;
            MusicPlayerWithoutUI audioPlayer = new MusicPlayerWithoutUI();
            audioPlayer.play();
            Scanner scan = new Scanner(System.in);

            while (true)
            {
                System.out.println("1. pause");
                System.out.println("2. resume");
                System.out.println("3. restart");
                System.out.println("4. stop");
                System.out.println("5. Jump to specific time");
                int c = scan.nextInt();
                audioPlayer.gotoChoice(c);
                if (c == 4)
                    break;
            }

        }

        catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    public static String getSongName(int songNo){

        String[] songs = playList();
        System.out.println();
        System.out.println("Playing song: "+songs[songNo-1].substring(0,songs[songNo-1].lastIndexOf(".")));
        String songName = songs[songNo-1];
        return songName;
    }


    public static void main(String[] args)
    {
        boolean quit = false;
        Scanner sc = new Scanner(System.in);
        int songNo=0;
        while(!quit) {
            System.out.println("Welcome, to music player!!");
            System.out.println("Please select a song to Play:");
            printSongsList();

            System.out.print("Enter song no or Press 0 to exit: ");

            songNo = sc.nextInt();
            if (songNo==0){
                quit=true;
                break;
            }
            String songName = getSongName(songNo);

            playerFunctions(songName);
        }
    }

    public void stop(){
        if(!clip.isRunning()){
            System.out.println("Song already stopped");
            return;
        }
        clip.setFramePosition(0);
        clip.stop();
    }

    public void jump(long seekPos){
        currentFrame = seekPos;
        System.out.println(clip.getMicrosecondPosition());

        clip.setMicrosecondPosition(seekPos);
        System.out.println(clip.getMicrosecondPosition());

        this.play();
    }

    private void gotoChoice(int c)
            throws Exception
    {
        switch (c)
        {
            case 1:
                pause();
                break;
            case 2:
                resume();
                break;
            case 3:
                restart();
                break;
            case 4:
                stop();
                break;
            case 5:
                System.out.println("Enter time (" + 0 +
                        ", " + clip.getMicrosecondLength() + ")");
                Scanner sc = new Scanner(System.in);
                long c1 = sc.nextLong();
                jump(c1);
                break;

        }

    }
}
