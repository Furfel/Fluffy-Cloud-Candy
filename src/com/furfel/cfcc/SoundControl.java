package com.furfel.cfcc;
import java.io.*;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;


public class SoundControl implements Runnable {

	Thread thread = new Thread(this);
	static File file;
	static SourceDataLine line;
	static double seconds;
	static AudioInputStream stream;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void loadAudio(String fn) {
		try {
			file = new File(fn);
			stream = AudioSystem.getAudioInputStream(file);
			AudioFormat af = stream.getFormat();
			
			if(af.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				af= new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, af.getSampleRate(), af.getSampleSizeInBits() * 2, af.getChannels(), af.getFrameSize() * 2, af.getFrameRate(), true);
				stream = AudioSystem.getAudioInputStream(af, stream);
			}
			
			SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), (int) (stream.getFrameLength() * af.getFrameSize()));
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(stream.getFormat());
		} catch(Exception e) {}
	}
	
	public static void startAudio() {
		line.start();
		FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue((float) (Math.log(1.0f) / Math.log(10.0f) * 20.0f));
		
		BooleanControl muted = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
		muted.setValue(false);
		
		FloatControl pan = (FloatControl) line.getControl(FloatControl.Type.PAN);
		pan.setValue(0.0f);
		
		long last = System.currentTimeMillis();
		double sincelast = (System.currentTimeMillis() - last) / 1000.0d;
		
		while(sincelast<seconds) {
			sincelast = (System.currentTimeMillis() - last) / 1000.0d;
		}
		
		try {
		int num_read =0;
		byte[] buf = new byte[line.getBufferSize()];
		while( (num_read = stream.read(buf, 0, buf.length)) >= 0) {
			int offset=0;
			 while(offset<num_read) {
				 offset += line.write(buf, offset, num_read-offset);
			 }
		}
		} catch(Exception e) {System.out.println(e.getMessage());}
		
		line.drain();
		line.stop();
		
		
	}
	
}
