package org.spatch;

public class WrapError extends Exception {
		private static final long serialVersionUID = 1L;


		public WrapError(String msg) {
			super(msg);
		}
		public WrapError(String msg, Throwable t) {
			super(msg, t);
		}
}
