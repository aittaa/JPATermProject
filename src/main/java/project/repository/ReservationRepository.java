package project.repository;

import project.EMFSingleton;
import project.domain.*;
import project.service.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ReservationRepository {
    EntityManagerFactory emf = EMFSingleton.getInstance();

    public void save(Reservation Reservation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ////
            em.persist(Reservation);
            ////
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }

    }


    public Reservation findOneWithID(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Reservation reservation = null;

        try {
            tx.begin();
            ////

            reservation = em.find(Reservation.class, reservationId);

            ////

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }

        return reservation;
    }


    public List<Reservation> findById(Long id) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        List<Reservation> reservations = null;

        try {
            tx.begin();
            ////
            User user = em.find(User.class, id);
            TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r", Reservation.class);
            reservations = query.getResultList();

            ////

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }

        return reservations;

    }


    public void removeReservation(Long userId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ////

            Reservation reservation = findOneWithID(userId);
            em.remove(reservation);

            ////

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }

    }

    public List<Reservation> findAll() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        List<Reservation> reservations = null;


        try {
            tx.begin();
            ////

            reservations = em.createQuery("select r from Reservation r", Reservation.class).getResultList();

            ////

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }

        return reservations;
    }

    public void reservate() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        UserService userService = new UserService();
        ReservationService reservationService = new ReservationService();
        ScreeningService screeningService = new ScreeningService();
        SeatService seatService = new SeatService();
        ReservationSeatService reservationSeatService = new ReservationSeatService();
        ScreeningSeatService screeningSeatService = new ScreeningSeatService();

        try {
            tx.begin();

            User user = userService.findOneWithID(1L);

            // ???????????? ????????? ?????? ????????? ????????? ??????
            TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r where r.user=:userId", Reservation.class);
            query.setParameter("userId", user);
            List<Reservation> reservations = query.getResultList();

//            List<Reservation> reservations = reservationService.findById(user.getUserId());
            if (reservations.isEmpty()) {
                System.out.println("????????? ????????????.");
            } else {
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                }
            }

            // ?????? ?????? ?????? ?????? ??????
            Screening screening = screeningService.findOneWithID(52L);
            System.out.println(screening);

            // 2 ?????? ??????
            // Reservation
            reservationService.createReservation(user, screening, ReservationStatusType.RESERVATION);

            // id ?????? ??????
            Reservation newReservation = reservationService.findOneWithID(60L);

            // Selected Seat id ?????? ??????
            Seat seat1 = seatService.findOneWithID(40L);
            Seat seat2 = seatService.findOneWithID(41L);

            // Create Reservation Seat
            reservationSeatService.createReservationSeat(newReservation, seat1);
            reservationSeatService.createReservationSeat(newReservation, seat2);

            // Update Screening Seat id ?????? ??????
            screeningSeatService.updateWithSeatIdAndScreeningId(seat1, screening, SeatStatusType.RESERVED);
            screeningSeatService.updateWithSeatIdAndScreeningId(seat2, screening, SeatStatusType.RESERVED);

            // ???????????? ????????? ?????? ????????? ????????? ??????
            TypedQuery<Reservation> query3 = em.createQuery("SELECT r FROM Reservation r where r.user=:userId", Reservation.class);
            query3.setParameter("userId", user);
            reservations = query3.getResultList();

//            List<Reservation> reservations = reservationService.findById(user.getUserId());
            if (reservations.isEmpty()) {
                System.out.println("????????? ????????????.");
            } else {
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                }
            }

            // ?????? ??? ???????????? ???????????? ?????? ?????? ?????? ?????? ??????
            TypedQuery<Reservation> query2 = em.createQuery("SELECT r FROM Reservation r", Reservation.class);
            List<Reservation> reservationList = query2.getResultList();

            if (reservations.isEmpty()) {
                System.out.println("????????? ????????????.");
            } else {
                for (Reservation reservation : reservationList) {
                    System.out.println(reservation);
                }
            }

//          ?????? ???, ???????????? ???????????? ?????? ????????? ?????????????????? ??????
            screeningService.findOneWithID(52L);

            ////

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }
    }

    public void cancleReservation() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            UserService userService = new UserService();
            ReservationService reservationService = new ReservationService();
            ScreeningService screeningService = new ScreeningService();
            SeatService seatService = new SeatService();
            ReservationSeatService reservationSeatService = new ReservationSeatService();
            ScreeningSeatService screeningSeatService = new ScreeningSeatService();

            User user = em.find(User.class, 1L);
            TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r where r.user=:userId", Reservation.class);
            query.setParameter("userId", user);
            Reservation reservation = query.getResultList().get(0);
            System.out.println(reservation);

//            ?????? ????????? ?????? ??????
            em.remove(reservation);

            // ?????? ?????? ????????? ?????? ????????? ?????? ???????????? ??????
            List<Reservation> reservationList = em.createQuery("select r from Reservation r", Reservation.class).getResultList();
            if (reservationList.isEmpty()) {
                System.out.println("?????? ????????? ????????????.");
            } else {
                for (Reservation res : reservationList) {
                    System.out.println(res);
                }
            }


            // ?????? ?????? ??????
            for (ReservationSeat reservationSeat : reservation.getReservationSeats()) {
                screeningSeatService.updateWithSeatIdAndScreeningId(reservationSeat.getSeat(), reservation.getScreening(), SeatStatusType.AVAILABLE);
            }

            // ?????? ???, ???????????? ???????????? ?????? ????????? ?????????????????? ??????
            screeningService.findOneWithID(52L);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            System.out.println("rollback");

        } finally {
            em.close();
        }
    }
}


