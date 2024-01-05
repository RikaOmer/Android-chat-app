import mongoose from "mongoose";
const scheme = mongoose.Schema;

const User = new scheme({
  username: {
    type: String,
    required: true,
    unique: true,
  },
  displayName: {
    type: String,
    required: true,
  },
  profilePic: {
    type: String,
    required: true,
  },
});

export default mongoose.model("User", User);
