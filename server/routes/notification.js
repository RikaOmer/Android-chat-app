import express from 'express';
import notificationService from '../services/notification.js';
const router = express.Router();
router
  .route('/')
  .post(notificationService.addUserToken)
  .get(notificationService.getUserToken)
  .delete(notificationService.deleteUserToken);

export default router;
