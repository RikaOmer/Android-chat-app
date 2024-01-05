import express from 'express';
import userServices from '../services/users.js';
const router = express.Router();
router.route('/').get(userServices.getUsers).post(userServices.createUser);

router.route('/:username').get(userServices.getUser);

export default router;
