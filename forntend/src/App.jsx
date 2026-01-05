import React, { useState } from 'react';
import axios from 'axios';
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from '@mui/material';

export default function App() {
  const [emailContent, setEmailContent] = useState('');
  const [tone, setTone] = useState('');
  const [generatedEmail, setGeneratedEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  // Sample tone options
  const tones = [
    { value: 'formal', label: 'Formal' },
    { value: 'informal', label: 'Informal' },
    { value: 'friendly', label: 'Friendly' },
    { value: 'professional', label: 'Professional' },
  ];

  const handleSubmit = async () => {
    setLoading(true);
    setErrorMsg('');
    setGeneratedEmail('');

    try {
      const response = await axios.post('http://localhost:8081/api/email/generate', {
        emailContent,
        tone,
      });

      setGeneratedEmail(
        typeof response.data === 'string'
          ? response.data
          : JSON.stringify(response.data)
      );
    } catch (error) {
      setErrorMsg('Failed to generate reply. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 5 }}>
      <Typography variant="h4" gutterBottom>
        AI Email Reply Generator
      </Typography>

      <Box sx={{ mx: 3 }}>
        <TextField
          label="Original Email Content"
          multiline
          rows={6}
          fullWidth
          value={emailContent}
          onChange={(e) => setEmailContent(e.target.value)}
          margin="normal"
        />

        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel>Tone (optional)</InputLabel>
          <Select
            value={tone || ''}
            label="Tone"
            onChange={(e) => setTone(e.target.value)}
          >
            {tones.map((t) => (
              <MenuItem key={t.value} value={t.value}>
                {t.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <Button
          variant="contained"
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? <CircularProgress size={24} /> : 'Generate Reply'}
        </Button>
      </Box>

      {errorMsg && (
        <Typography color="error" sx={{ mt: 2 }}>
          {errorMsg}
        </Typography>
      )}

      {generatedEmail && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            Generated Reply:
          </Typography>
          <TextField
            multiline
            rows={6}
            fullWidth
            value={generatedEmail}
            inputProps={{ readOnly: true }}
          />
          <Button
            variant="outlined"
            sx={{ mt: 1 }}
            onClick={() => navigator.clipboard.writeText(generatedEmail)}
          >
            Copy to Clipboard
          </Button>
        </Box>
      )}
    </Container>
  );
}
