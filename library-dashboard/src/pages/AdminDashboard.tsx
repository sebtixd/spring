import { useState } from 'react';
import api from '../api/client';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import toast from 'react-hot-toast';

interface BookForm {
  isbn: string;
  title: string;
  author: string;
  totalCopies: number;
}

interface Loan {
  id: number;
  bookId: string;
  userEmail: string;
  loanDate: string;
  dueDate: string;
  status: string;
}

export default function AdminDashboard() {
  const queryClient = useQueryClient();
  const [form, setForm] = useState<BookForm>({
    isbn: '',
    title: '',
    author: '',
    totalCopies: 1,

  });

  // Fetch books
  const { data: books = [] } = useQuery({
    queryKey: ['books'],
    queryFn: () => api.get('/books').then(res => res.data),
  });

  // Fetch loans
  const { data: loans = [] } = useQuery({
    queryKey: ['loans'],
    queryFn: () => api.get('/loans').then(res => res.data),
  });

  // Add book mutation
const addBookMutation = useMutation({
  mutationFn: (newBook: BookForm) => 
    api.post('/books', newBook, {
      headers: {
        'Content-Type': 'application/json'
      }
    }),
  onSuccess: () => {
    toast.success('Book added!');
    queryClient.invalidateQueries({ queryKey: ['books'] });
    setForm({ isbn: '', title: '', author: '', totalCopies: 1 });
  },
  onError: (err: any) => {
    const errorMessage = err.response?.data?.message || 
                        err.response?.data || 
                        err.message || 
                        'Failed to add book';
    toast.error(errorMessage);
  },
});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    addBookMutation.mutate(form);
  };

 return (
  <div className="min-h-screen bg-gradient-to-b from-purple-50 to-blue-50">
    <header className="bg-gradient-to-r from-purple-800 to-blue-800 text-white shadow-2xl">
      <div className="max-w-7xl mx-auto px-6 py-8">
        <h1 className="text-4xl font-bold">Admin Panel</h1>
        <p className="text-purple-100 mt-2">Manage books and monitor loans</p>
      </div>
    </header>

    <div className="max-w-7xl mx-auto px-6 py-12">
      {/* Add Book Form */}
      <div className="bg-white rounded-3xl shadow-xl p-10 mb-12">
        <h2 className="text-3xl font-bold text-gray-800 mb-8">Add New Book</h2>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <input
            type="text"
            placeholder="ISBN *"
            required
            value={form.isbn}
            onChange={(e) => setForm(f => ({ ...f, isbn: e.target.value }))}
className="px-6 py-4 bg-white text-gray-900 placeholder-gray-500 border-2 border-gray-300 rounded-xl focus:outline-none focus:border-purple-500 transition"          />
          <input
            type="text"
            placeholder="Title *"
            required
            value={form.title}
            onChange={(e) => setForm(f => ({ ...f, title: e.target.value }))}
className="px-6 py-4 bg-white text-gray-900 placeholder-gray-500 border-2 border-gray-300 rounded-xl focus:outline-none focus:border-purple-500 transition"          />
          <input
            type="text"
            placeholder="Author *"
            required
            value={form.author}
            onChange={(e) => setForm(f => ({ ...f, author: e.target.value }))}
className="px-6 py-4 bg-white text-gray-900 placeholder-gray-500 border-2 border-gray-300 rounded-xl focus:outline-none focus:border-purple-500 transition"          />
          <input
            type="number"
            placeholder="Total Copies *"
            min="1"
            required
            value={form.totalCopies}
            onChange={(e) => setForm(f => ({ ...f, totalCopies: Number(e.target.value) }))}
className="px-6 py-4 bg-white text-gray-900 placeholder-gray-500 border-2 border-gray-300 rounded-xl focus:outline-none focus:border-purple-500 transition"          />
          <div className="md:col-span-2">
            <button
              type="submit"
              disabled={addBookMutation.isPending}
              className="w-full md:w-auto px-12 py-4 bg-gradient-to-r from-purple-600 to-blue-600 text-white font-bold rounded-xl hover:from-purple-700 hover:to-blue-700 transform hover:scale-105 transition disabled:opacity-70"
            >
              {addBookMutation.isPending ? 'Adding Book...' : 'Add Book'}
            </button>
          </div>
        </form>
      </div>

      {/* Loans Table */}
      <div className="bg-white rounded-3xl shadow-xl p-10">
        <h2 className="text-3xl font-bold text-gray-800 mb-8">All Loans ({loans.length})</h2>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b-2 border-gray-300">
                <th className="text-left py-4 px-6 font-semibold text-gray-700">Loan ID</th>
                <th className="text-left py-4 px-6 font-semibold text-gray-700">User</th>
                <th className="text-left py-4 px-6 font-semibold text-gray-700">Book ID</th>
                <th className="text-left py-4 px-6 font-semibold text-gray-700">Loan Date</th>
                <th className="text-left py-4 px-6 font-semibold text-gray-700">Due Date</th>
                <th className="text-left py-4 px-6 font-semibold text-gray-700">Status</th>
              </tr>
            </thead>
<tbody>
  {loans.map((loan: Loan) => (
    <tr key={loan.id} className="border-b hover:bg-gray-50 transition">
      <td className="py-4 px-6 text-gray-900">{loan.id}</td>
      <td className="py-4 px-6 font-medium text-gray-900">{loan.userEmail}</td>
      <td className="py-4 px-6 text-gray-900">{loan.bookId}</td>
      <td className="py-4 px-6 text-gray-900">{new Date(loan.loanDate).toLocaleDateString()}</td>
      <td className="py-4 px-6 text-gray-900">{new Date(loan.dueDate).toLocaleDateString()}</td>
      <td className="py-4 px-6">
        <span className={`px-4 py-2 rounded-full text-sm font-medium ${
          loan.status === 'ACTIVE'
            ? 'bg-green-100 text-green-800'
            : 'bg-red-100 text-red-800'
        }`}>
          {loan.status}
        </span>
      </td>
    </tr>
  ))}
</tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
);
}