import React, { useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import DeleteIcon from "@mui/icons-material/Delete";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import Api from "../../Helpers/Api";
import Box from "@mui/material/Box";
import Alert from "@mui/material/Alert";
import AlertTitle from "@mui/material/AlertTitle";

// const CreatePoll = ({ tripId, userId }) => {
const CreatePoll = ({ tripId, userId, setPolls }) => {
  // const tripId = 1;
  // const userId = 1;
  const [open, setOpen] = useState(false);
  const [question, setQuestion] = useState("");
  const [options, setOptions] = useState([{ option: "" }]);
  const [error, setError] = useState(null);

  const handleClickOpen = () => {
    console.log(options);
    setOpen(true);
  };

  const handleClose = () => {
    console.log(options);
    setOpen(false);
    setQuestion("");
    setOptions([{ option: "" }]);
    setError(null);
  };

  const handleOptionAdd = () => {
    setOptions([...options, { option: "" }]);
  };

  const handleOptionRemove = (index) => {
    const list = [...options];
    list.splice(index, 1);
    setOptions(list);
  };

  const handleOptionChange = (e, index) => {
    const { name, value } = e.target;
    const list = [...options];
    list[index][name] = value;
    setOptions(list);
  };

  const handleSubmit = () => {
    setError(null);
    const nonEmptyOptions = options.filter((o) => o.option !== "");
    if (question != "" && nonEmptyOptions.length > 1) {
      const details = nonEmptyOptions.map((o) => o.option);
      details.unshift(question);
      console.log(details);
      Api.createPoll(tripId, userId, details)
        .then((res) => {
          if (!res.ok) {
            //http OK from server
            console.log("error");
            throw Error("could not fetch data");
          }
          return res.json(); //return another promise of data
        })
        .then((data) => {
          setPolls(data);
          setQuestion("");
          setOptions([{ option: "" }]);
          setOpen(false);
        })
        .catch((err) => {
          console.log(err.message);
        });
    } else if (question == "") {
      setError("Question cannot be empty");
      // setTimeout(() => {
      //   setError(null);
      // }, 3000);
    } else {
      setError("There must be more than 1 non-empty option");
      // setTimeout(() => {
      //   setError(null);
      // }, 3000);
    }
  };

  return (
    <div>
      <Box sx={{ paddingLeft: 2 }}>
        <IconButton onClick={handleClickOpen}>
          <AddRoundedIcon />
        </IconButton>
      </Box>
      {/* <Button variant="outlined" onClick={handleClickOpen}>
        New Poll
      </Button> */}
      <Dialog
        open={open}
        onClose={handleClose}
        scroll="paper"
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle sx={{ display: "flex", alignItems: "center" }}>
          Create a Poll
          <IconButton sx={{ ml: "auto" }} onClick={handleClose}>
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent>
          <label>Question</label>
          <TextField
            autoFocus
            margin="dense"
            id="question"
            fullWidth
            maxWidth="md"
            required
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            placeholder="Type your question here"
          />
          <Box sx={{ paddingTop: 2 }}></Box>
          <label htmlFor="option">Option(s)</label>
          {options.map((singleOption, index) => {
            return (
              <div key={index} className="rowComponent">
                <div>
                  <TextField
                    autoFocus
                    margin="dense"
                    id={`option-${index}`}
                    name="option"
                    width="300px"
                    placeholder="Type an option here"
                    required
                    value={singleOption.option}
                    onChange={(e) => handleOptionChange(e, index)}
                  />
                </div>
                {options.length > 1 && (
                  <Box sx={{ paddingLeft: 2 }}>
                    <IconButton onClick={() => handleOptionRemove(index)}>
                      <DeleteOutlineOutlinedIcon />
                    </IconButton>
                  </Box>
                )}
              </div>
            );
          })}
          <Button onClick={handleOptionAdd}>Add another option</Button>
          {error && (
            <Alert severity="error">
              <AlertTitle>{error}</AlertTitle>
            </Alert>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleSubmit}>Create</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default CreatePoll;
