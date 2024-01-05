import isExists from "../controllers/userPass.js";

const isExists = async (req, res) => {
  res.json(
    await userPassController.isExists(req.params.username, req.params.password)
  );
};

export default { isExists };
