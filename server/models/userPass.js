import mongoose from "mongoose";

const scheme = mongoose.Schema;

const UserPass = new scheme({
  username: {
    type: String,
    required: true,
    unique: true,
  },
  password: {
    type: String,
    required: true,
  },
});

export default mongoose.model("UserPass", UserPass);