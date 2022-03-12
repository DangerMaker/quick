package com.hundsun.zjfae.common.view.cropview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class Crop {
	public static final int REQUEST_PICK = 9162;
	public static final int REQUEST_CROP = 6709;
	public static final int RESULT_ERROR = 404;

	public interface Extra {
		String ASPECT_X = "aspect_x";
		String ASPECT_Y = "aspect_y";
		String OUTPUT_X = "output_x";
		String OUTPUT_Y = "output_y";
		String ERROR = "error";
	}

	private Intent cropIntent;

	public static Crop of(Uri source, Uri destination) {
		return new Crop(source, destination);
	}

	private Crop(Uri source, Uri destination) {
		cropIntent = new Intent();
		cropIntent.setData(source);
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
	}

	public Crop asSquare() {
		cropIntent.putExtra(Extra.ASPECT_X, 1);
		cropIntent.putExtra(Extra.ASPECT_Y, 1);
		return this;
	}

	public Crop withAspect(int x, int y) {
		cropIntent.putExtra(Extra.ASPECT_X, x);
		cropIntent.putExtra(Extra.ASPECT_Y, y);
		return this;
	}

	public Crop withOutputSize(int width, int height) {
		cropIntent.putExtra(Extra.OUTPUT_X, width);
		cropIntent.putExtra(Extra.OUTPUT_Y, height);
		return this;
	}

	public Intent getIntent() {
		return cropIntent;
	}

	public static Uri getSourceUri(Intent intent) {
		return intent.getData();
	}

	public static Uri getSaveUri(Intent intent) {
		return intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
	}

	public static int getAspectX(Bundle extras) {
		return extras.getInt(Extra.ASPECT_X);
	}

	public static int getAspectY(Bundle extras) {
		return extras.getInt(Extra.ASPECT_Y);
	}

	public static int getOutputX(Bundle extras) {
		return extras.getInt(Extra.OUTPUT_X);
	}

	public static int getOutputY(Bundle extras) {
		return extras.getInt(Extra.OUTPUT_Y);
	}

	public static Throwable getError(Intent result) {
		return (Throwable) result.getSerializableExtra(Extra.ERROR);
	}
}