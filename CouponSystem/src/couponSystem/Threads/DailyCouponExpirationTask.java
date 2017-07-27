package couponSystem.Threads;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import DAO.DB.CouponDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.connectionException;
import Facades.CompanyFacade;
import javaBeans.Coupon;

public class DailyCouponExpirationTask implements Runnable {

	private CouponDBDAO couponDBDAO;
	private CompanyFacade companyFacade;
	private Thread thread;

	/**
	 * constructs a daily task thread. initializes the dataBase DAOs required
	 * for the task.
	 * 
	 * @throws connectionException
	 *             if the database is down or a connection couldn't be
	 *             established
	 */

	public DailyCouponExpirationTask() throws connectionException {
		couponDBDAO = new CouponDBDAO();
		companyFacade = new CompanyFacade(0);
	}

	/**
	 * runnable tasked with removing expired coupons from the system.
	 * 
	 * when the thread is first activated, the time until midnight is measured
	 * and the thread is put to sleep for that length of time. the thread will
	 * wake up when it is midnight and will then perform the task of deleting
	 * all coupons which have expired:
	 * 
	 * any coupon with a past end date will be removed from the entire system,
	 * including: coupon table, company-coupon table and the customer-coupon
	 * table. total tables affected: 3.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				// calculate time until midnight tonight, then put thread to
				// sleep until then.
				long sleepTime = (getMidnight().getTimeInMillis() - System.currentTimeMillis());
				System.out.println("time to sleep: " + sleepTime);
				Thread.sleep(sleepTime);

				// thread woke up, it is now midnight------------
				System.out.println(Thread.currentThread().getName() + " im awake!!!!");
				deleteAllExpiredCoupons();
			}
		} catch (

		InterruptedException e) {

			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + "  task has been interrupted!!");
		}

	}

	/**
	 * stopTask interrupts the currently running thread.if the thread is in the
	 * middle of performing the task this will stop the thread from performing
	 * the task fully and will prevent the next expired coupon from getting
	 * deleted .
	 */
	public void stopTask() {
		thread.interrupt();
		System.out.println(thread.getName() + " this is thread we interrupted");

	}

	/**
	 * this method is required in order to keep a reference to the thread that
	 * is using the runnable target of this class
	 */

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * Convenience method which deletes all expired coupons from the system :any
	 * coupon with a past end date will be removed from the entire system,
	 * including: coupon table, company-coupon table and the customer-coupon
	 * table. total tables affected: 3.
	 * 
	 * the interrupted status of the thread is checked after each coupon
	 * deletion, so if the thread is interrupted the next coupon will not be
	 * deleted.
	 */

	private void deleteAllExpiredCoupons() {
		Set<Coupon> expiredBeforeToday;
		try {
			expiredBeforeToday = couponDBDAO.getCouponUntillEndDate(getMidnight().getTime());
			Iterator<Coupon> iterator = expiredBeforeToday.iterator();
			while (iterator.hasNext()) {
				Coupon couponElement = iterator.next();
				if (!Thread.currentThread().isInterrupted()) {
					companyFacade.removeCoupon(couponElement);
					System.out.println("deleted coupon number " + couponElement.getCoupon_id() + " coupon title "
							+ couponElement.getTitle());
				}

			}
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Convenience method which creates a calendar instance and sets the time to
	 * midnight.
	 * 
	 * @return Calendar instance representing the nearest midnight
	 */
	private Calendar getMidnight() {
		Calendar midnight = Calendar.getInstance();
		midnight.add(Calendar.DAY_OF_MONTH, 1);

		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.MINUTE, 0);
		midnight.set(Calendar.SECOND, 0);
		midnight.set(Calendar.MILLISECOND, 0);
		return midnight;

	}

}
