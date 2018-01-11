package com.jsd.filing.mainProc;

import com.jsd.filing.utils.DigestMesg;
import com.jsd.filing.utils.PicUtils;
import com.jsd.filing.utils.Utils;

public class UtensilTools {
	private ThreadLocal<DigestMesg> digmsgTl = new ThreadLocal<DigestMesg>() {
		@Override
		protected DigestMesg initialValue() {
			// TODO Auto-generated method stub
			return new DigestMesg();
		}
	};
	private ThreadLocal<Utils> cpTools = new ThreadLocal<Utils>() {
		@Override
		protected Utils initialValue() {
			// TODO Auto-generated method stub
			return new Utils();
		}
	};
	
	private ThreadLocal<PicUtils> picTools = new ThreadLocal<PicUtils>() {
		@Override
		protected PicUtils initialValue() {
			// TODO Auto-generated method stub
			return new PicUtils();
		}
	};
	public DigestMesg getDigmsg() {
		return digmsgTl.get();
	}
	public Utils getCpTools() {
		return cpTools.get();
	}
	public PicUtils getPicTools() {
		return picTools.get();
	}
}